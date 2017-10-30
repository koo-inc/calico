package jp.co.freemind.calico.servlet;

import static com.google.common.base.MoreObjects.firstNonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.endpoint.Dispatcher;
import jp.co.freemind.calico.core.endpoint.EndpointResolver;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;
import jp.co.freemind.calico.core.exception.ViolationException;
import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.log.LoggingSessionStarter;
import jp.co.freemind.calico.core.util.FileBackedInputStream;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.UnhandledException;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.util.CookieUtil;
import jp.co.freemind.calico.servlet.util.NetworkUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;

@Log4j2
public class RequestSession {
  public void execute(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse res) {
    try {
      doInTransaction(servletConfig, req, res, () -> {
        RequestParam param = getRequestParam(servletConfig, req, res);
        Context context = doGetContext(param);

        getTransactionScopedZone(context, param).run(() -> {
          LoggingSession loggingSession = getLoggingSessionStarter().start();
          Zone.getCurrent().fork(s -> s
            .scope(TransactionScoped.class)
            .provide(Keys.LOGGING_SESSION, loggingSession)
            .onError(e -> {
              loggingSession.error(e);
              renderError(context, param, e);
              throw e;
            })
            .onFinish(() -> loggingSession.finish(res.getStatus()))
          ).run(() -> {
            InputStream is = Zone.getCurrent().getInstance(Keys.INPUT);
            Object output = new Dispatcher(getEndpointResolver()).dispatch(param.getPath(), is, getInterceptionHandlers(servletConfig));
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

  protected void doInTransaction(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse res, Runnable block) {
    Zone.getCurrent().getInstance(Config.class).getTransactionManager()
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
    return new RequestParam(conf, req, res);
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

  @Getter
  protected static class RequestParam {
    private final String path;
    private final String remoteAddress;
    private final LocalDateTime processDatetime;
    private final AuthToken authToken;
    private final FileBackedInputStream payload;
    private final ServletConfig config;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    RequestParam(ServletConfig conf, HttpServletRequest req, HttpServletResponse res) {
      this.path = req.getPathInfo();
      this.remoteAddress = firstNonNull(NetworkUtil.getRemoteAddrWithConsiderForwarded(req), "(unknown)");
      this.processDatetime = LocalDateTime.now();
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
  }
}
