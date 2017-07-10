package calicosample.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import calicosample.core.LogDbSetting;
import calicosample.dao.log.LoggingDao;
import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.ErrorLog;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.util.StreamUtil;
import jp.co.freemind.calico.servlet.util.NetworkUtil;

public class AccessLoggingSession implements LoggingSession {
  private final LoggingDao loggingDao;
  private final LogDbSetting setting;
  private final AccessStartLog startLog;
  private final HttpServletRequest request;
  private boolean finished = false;

  @Inject
  public AccessLoggingSession(
    LoggingDao loggingDao,
    LogDbSetting setting,
    AuthToken authToken,
    HttpServletRequest request,
    @Named("input") InputStream is
  ) {
    this.loggingDao = loggingDao;
    this.setting = setting;
    this.request = request;

    this.startLog = new AccessStartLog();
    this.startLog.setLoginId(authToken.getId());
    this.startLog.setRequestUri(request.getRequestURI());
    this.startLog.setRefererPath(getRefererPath(request));
    this.startLog.setCookies(collectAllCookies(request));
    this.startLog.setParams(parsePayload(is));
    this.startLog.setRemoteAddr(NetworkUtil.getRemoteAddrWithConsiderForwarded(request));
    this.startLog.setHostAddr(request.getRemoteHost());

    if (setting.getJdbcUrl() != null) {
      loggingDao.insert(this.startLog);
    }
  }

  @Override
  public void finish(int resultCode) {
    if (finished) return;
    finished = true;

    if (setting.getJdbcUrl() == null) return;

    AccessEndLog endLog = new AccessEndLog();
    endLog.setStartLog(this.startLog);
    endLog.setResultCode(String.valueOf(resultCode));

    loggingDao.insert(endLog);
  }

  @Override
  public void finish(Throwable t) {
    if (finished) return;
    finished = true;

    if (setting.getJdbcUrl() == null) return;

    ErrorLog errorLog = new ErrorLog();
    errorLog.setStartLog(this.startLog);
    errorLog.setException(Throwables.getStackTraceAsString(t));
    errorLog.setHeaders(collectAllHeaders(this.request));
  }

  private static Pattern REFERER = Pattern.compile("^(?:https?://[^/]+)(/[^?]*)?$");
  private String getRefererPath(HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    if (referer == null) return null;

    Matcher matcher = REFERER.matcher(referer);
    if (matcher.find()) {
      return matcher.group(1);
    }
    else {
      return null;
    }
  }

  private static String collectAllCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return "";

    return Stream.of(cookies)
      .map(c -> c.getName() + "=" + c.getValue())
      .collect(Collectors.joining("\n"));
  }

  static String collectAllHeaders(HttpServletRequest request) {
    if (request.getHeaderNames() == null) return "";

    Stream<String> name = StreamUtil.of(request.getHeaderNames());
    Stream<Stream<String>> values = StreamUtil.of(request.getHeaderNames()).map(request::getHeaders).map(StreamUtil::of);
    return StreamUtil.zip(name, values)
      .flatMap(tuple -> tuple.getValue2().map(v -> tuple.getValue1() + ": " + v))
      .collect(Collectors.joining("\n"));
  }

  private static final Set<String> MASK_NEED_KEYS = Sets.newHashSet("password", "payload");

  private static String parsePayload(InputStream is) {
    if (!is.markSupported()) {
      return "(not supported)";
    }

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
    catch (IOException e) {
      return "(cannnot parse: " + e.getMessage() + ")";
    }
    finally {
      try {
        is.reset();
      }
      catch (IOException ignored) {}
    }
  }
}
