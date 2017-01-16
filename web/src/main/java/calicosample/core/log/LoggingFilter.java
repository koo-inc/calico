package calicosample.core.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.ext.Provider;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.service.system.LoggingService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.util.LoggingUtil;
import jp.co.freemind.calico.util.NetworkUtil;

@Provider
@PreMatching
@Priority(Integer.MIN_VALUE)
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
  @javax.ws.rs.core.Context
  private HttpServletRequest request;
  @javax.ws.rs.core.Context
  private HttpServletResponse response;
  @javax.ws.rs.core.Context
  private ResourceInfo resourceInfo;
  @Inject private javax.inject.Provider<Context<CalicoSampleAuthInfo>> contextProvider;
  @Inject private javax.inject.Provider<AccessStartLog> accessStartLogProvider;
  @Inject private LoggingService loggingService;

  private static final Set<String> MASK_NEED_KEYS = Sets.newHashSet("password", "payload");

  @Override
  public void filter(ContainerRequestContext ctx) throws IOException {
    FileBackedInputStream is = new FileBackedInputStream(ctx.getEntityStream(), 1_000_000);
    ctx.setEntityStream(is);

    CalicoSampleAuthInfo authInfo = contextProvider.get().getAuthInfo();
    AccessStartLog log = accessStartLogProvider.get();

    is.mark(0);
    try {
      log.setUserId(authInfo.getUserId());
      log.setLoginId(authInfo.getLoginId());
      log.setHostAddr(request.getLocalAddr());
      log.setRemoteAddr(NetworkUtil.getRemoteAddrWithConsiderForwarded(request));
      log.setRequestUri(request.getRequestURI());
      log.setRequestMethod(request.getMethod());
      log.setCookies(LoggingUtil.collectAllCookies(request));
      log.setParams(parsePayload(is));
    }
    catch (Exception e) {
      loggingService.insert(log);
      loggingService.insert(ErrorLog.of(log, e, LoggingUtil.collectAllHeaders(request)));
      return;
    }
    finally {
      try {
        is.reset();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    loggingService.insert(log);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    AccessStartLog startLog = accessStartLogProvider.get();
    try {
      AccessEndLog endLog = AccessEndLog.of(startLog);
      endLog.setResultCode(String.valueOf(containerResponseContext.getStatus()));
      loggingService.insert(endLog);
    }
    catch (Exception e) {
      loggingService.insert(ErrorLog.of(startLog, e, LoggingUtil.collectAllHeaders(request)));
    }
  }

  private String parsePayload(InputStream is) throws IOException {
    try {
      JsonFactory factory = new JsonFactory();
      JsonParser parser = factory.createParser(is);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      JsonGenerator generator = factory.createGenerator(os);

      boolean needsMasked = false;

      while (parser.nextToken() != null) {
        switch (parser.getCurrentToken()) {
          case NOT_AVAILABLE:
            throw new IllegalStateException("不正なパラメータ");
          case START_OBJECT:
            generator.writeStartObject();
            break;
          case END_OBJECT:
            generator.writeEndObject();
            break;
          case START_ARRAY:
            generator.writeStartArray();
            break;
          case END_ARRAY:
            generator.writeEndArray();
            break;
          case FIELD_NAME:
            String name = parser.getText();
            if (MASK_NEED_KEYS.contains(name)) {
              needsMasked = true;
            }
            generator.writeFieldName(name);
            break;
          case VALUE_EMBEDDED_OBJECT:
            break;
          case VALUE_STRING:
            String value = parser.getText();
            if (needsMasked) {
              needsMasked = false;
              generator.writeString("***");
            }
            else {
              generator.writeString(value);
            }
            break;
          case VALUE_NUMBER_INT:
            if (needsMasked) needsMasked = false;
            generator.writeNumber(parser.getText());
            break;
          case VALUE_NUMBER_FLOAT:
            if (needsMasked) needsMasked = false;
            generator.writeNumber(parser.getText());
            break;
          case VALUE_TRUE:
            if (needsMasked) needsMasked = false;
            generator.writeBoolean(true);
            break;
          case VALUE_FALSE:
            if (needsMasked) needsMasked = false;
            generator.writeBoolean(false);
            break;
          case VALUE_NULL:
            if (needsMasked) needsMasked = false;
            generator.writeNull();
            break;
        }
      }
      generator.flush();
      return os.toString(Charsets.UTF_8.name());
    }
    finally {
      Closeables.closeQuietly(is);
    }
  }
}
