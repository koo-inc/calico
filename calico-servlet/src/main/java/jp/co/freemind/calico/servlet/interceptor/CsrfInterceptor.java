package jp.co.freemind.calico.servlet.interceptor;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.Keys;
import jp.co.freemind.calico.servlet.SessionSetting;

public class CsrfInterceptor implements EndpointInterceptor {
  private final Message insecureMessage;

  public CsrfInterceptor(Message insecureMessage) {
    this.insecureMessage = insecureMessage;
  }

  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    HttpServletRequest req = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST);
    AuthInfo authInfo = Zone.getContext().getAuthInfo().orElse(null);
    if (authInfo == null || !authInfo.isAuthenticated()) {
      return invocation.proceed();
    }
    if (authInfo.isSystemUsed()) {
      return invocation.proceed();
    }

    if (Objects.equals(getHeaderCsrfToken(req), getCsrfToken(authInfo))) {
      return invocation.proceed();
    }

    throw new AuthorizationException(insecureMessage);
  }

  private String getHeaderCsrfToken(HttpServletRequest req) {
    return req.getHeader(Zone.getCurrent().getInstance(SessionSetting.class).getCsrfTokenHeader());
  }

  private String getCsrfToken(AuthInfo authInfo) {
    return authInfo.getAuthToken().getCsrfToken();
  }
}
