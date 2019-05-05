package jp.co.freemind.calico.servlet;

import static com.google.common.base.MoreObjects.firstNonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.endpoint.Dispatcher;
import jp.co.freemind.calico.core.endpoint.EndpointInfo;
import jp.co.freemind.calico.core.endpoint.EndpointResolver;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;
import jp.co.freemind.calico.core.exception.UnknownEndpointException;
import jp.co.freemind.calico.core.exception.ViolationException;
import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.log.LoggingSessionStarter;
import jp.co.freemind.calico.core.util.FileBackedInputStream;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.UnhandledException;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.util.CookieUtil;
import jp.co.freemind.calico.servlet.util.NetworkUtil;
import org.apache.logging.log4j.Logger;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;

public class RequestSession {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(RequestSession.class);

  public void execute(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse res) {
    EndpointResolver resolver = getEndpointResolver();

    try {
      RequestParam param = getRequestParam(servletConfig, req, res);
      Optional<EndpointInfo> endpointInfo = resolver.resolve(param.getPath());

      doInTransaction(param, () -> {
        Context context = doGetContext(param);

        getTransactionScopedZone(context, param).run(() -> {
          LoggingSession loggingSession = getLoggingSessionStarter().start();
          Zone.getCurrent().fork(s -> s
            .scope(TransactionScoped.class)
            .provide(Keys.LOGGING_SESSION, loggingSession)
            .onError(e -> {
              Throwable t = UnhandledException.getPeeled(e);
              if (!isDesignedException(t)) {
                loggingSession.error(t);
              }
              renderError(context, param, t);
              throw t;
            })
            .onFinish(() -> loggingSession.finish(res.getStatus()))
          ).run(() -> {
            InputStream is = Zone.getCurrent().getInstance(Keys.INPUT);
            Object output = new Dispatcher(endpointInfo.orElseThrow(() -> new UnknownEndpointException(param.path)))
              .dispatch(is, getInterceptionHandlers(servletConfig));
            render(context, param, output);
          });
        });
      });
    }
    catch (Exception e) {
      Throwable t = UnhandledException.getPeeled(e);
      if (!isDesignedException(t)) {
        log.catching(t);
      }
    }
    catch (Throwable t) {
      log.catching(t);
      throw t;
    }
  }

  protected Config getConfig() {
    return Zone.getCurrent().getInstance(Config.class);
  }

  protected void doInTransaction(RequestParam param, Runnable block) {
    getConfig().getTransactionManager()
      .requiresNew(TransactionIsolationLevel.READ_COMMITTED, block);
  }

  protected Context getContext(RequestParam param) {
    AuthenticationProcedure authority = Zone.getCurrent().getInstance(AuthenticationProcedure.class);
    return new Context(s -> s
      .authInfo(authority.proceed(param.getAuthToken()))
      .processDateTime(param.getProcessDatetime())
      .remoteAddress(param.getRemoteAddress())
      .path(param.getPath())
    );
  }

  private Context doGetContext(RequestParam param) {
    try {
      return getContext(param);
    }
    catch (Throwable t) {
      renderError(null, param, t);
      throw t;
    }
  }

  protected RequestParam getRequestParam(ServletConfig conf, HttpServletRequest req, HttpServletResponse res) {
    return new RequestParam(conf, req, res, getProcessDateTime());
  }

  protected LocalDateTime getProcessDateTime() {
    return LocalDateTime.now();
  }

  protected Zone getTransactionScopedZone(Context context, RequestParam param) {
    return Zone.getCurrent().fork(s -> s
      .scope(TransactionScoped.class)
      .provide(Keys.CONTEXT, context)
      .provide(Keys.AUTH_TOKEN, param.getAuthToken())
      .provide(Keys.PATH, param.getPath())
      .provide(Keys.REMOTE_ADDRESS, param.getRemoteAddress())
      .provide(Keys.PROCESS_DATETIME, param.getProcessDatetime())
      .provide(Keys.SERVLET_REQUEST, param.getRequest())
      .provide(Keys.SERVLET_RESPONSE, param.getResponse())
      .provide(Keys.INPUT, param.getPayload())
    );
  }

  protected EndpointResolver getEndpointResolver() {
    return Zone.getCurrent().getInstance(EndpointResolver.class);
  }

  protected InterceptionHandler[] getInterceptionHandlers(ServletConfig servletConfig) {
    return (InterceptionHandler[]) servletConfig.getServletContext()
      .getAttribute(CalicoServletConfig.INTERCEPTOR_HANDLERS);
  }

  protected LoggingSessionStarter getLoggingSessionStarter() {
    return Zone.getCurrent().getInstance(LoggingSessionStarter.class);
  }

  protected void render(@Nonnull Context context, RequestParam param, Object output) {
    Zone.getCurrent().getInstance(Keys.DEFAULT_RENDERER).render(param.getConfig(), param.getResponse(), context, output);
  }

  protected void renderError(@Nullable Context context, RequestParam param, Throwable t) {
    Zone.getCurrent().getInstance(Keys.EXCEPTION_RENDERER).render(param.getConfig(), param.getResponse(), t);
  }

  protected boolean isDesignedException(Throwable t) {
    return t instanceof ViolationException;
  }

  protected static class RequestParam {
    private final String path;
    private final String remoteAddress;
    private final LocalDateTime processDatetime;
    private final AuthToken authToken;
    private final FileBackedInputStream payload;
    private final ServletConfig config;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    RequestParam(ServletConfig conf, HttpServletRequest req, HttpServletResponse res, LocalDateTime processDateTime) {
      this.path = req.getPathInfo();
      this.remoteAddress = firstNonNull(NetworkUtil.getRemoteAddrWithConsiderForwarded(req), "0.0.0.0");
      this.processDatetime = processDateTime;
      this.authToken = CookieUtil.getSessionToken(req)
        .map(AuthToken::of)
        .orElseGet(() -> AuthToken.createEmpty(remoteAddress, processDatetime));

      this.payload = new FileBackedInputStream(() -> {
        try {
          return req.getInputStream();
        } catch (IOException e) {
          e.printStackTrace();
          return new ByteArrayInputStream(new byte[0]);
        }
      }, 1024 * 1024);

      this.config = conf;
      this.request = req;
      this.response = res;
    }

    public String getPath() {
      return this.path;
    }

    public String getRemoteAddress() {
      return this.remoteAddress;
    }

    public LocalDateTime getProcessDatetime() {
      return this.processDatetime;
    }

    public AuthToken getAuthToken() {
      return this.authToken;
    }

    public FileBackedInputStream getPayload() {
      return this.payload;
    }

    public ServletConfig getConfig() {
      return this.config;
    }

    public HttpServletRequest getRequest() {
      return this.request;
    }

    public HttpServletResponse getResponse() {
      return this.response;
    }
  }
}
