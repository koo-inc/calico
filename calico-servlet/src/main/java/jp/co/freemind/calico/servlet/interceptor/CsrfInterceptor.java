package jp.co.freemind.calico.servlet.interceptor;

import java.util.Objects;
import java.util.Optional;

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
  private final boolean permitWhenAuthInfoIsNull;

  public CsrfInterceptor(Message insecureMessage) {
    this(insecureMessage, false);
  }
  public CsrfInterceptor(Message insecureMessage, boolean permitWhenAuthInfoIsNull) {
    this.insecureMessage = insecureMessage;
    this.permitWhenAuthInfoIsNull = permitWhenAuthInfoIsNull;
  }

  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    HttpServletRequest req = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST);
    Optional<AuthInfo> authInfo = Zone.getContext().getAuthInfo();
    if (authInfo.map(ai -> false).orElse(permitWhenAuthInfoIsNull)) {
      return invocation.proceed();
    }

    if (authInfo.map(AuthInfo::isUsedBySystem).orElse(false)) {
      return invocation.proceed();
    }

    verifyCsrfToken(req, authInfo.orElse(null));

    return invocation.proceed();
  }

  private void verifyCsrfToken(HttpServletRequest req, AuthInfo authInfo) {
    String headerCsrfToken = req.getHeader(Zone.getCurrent().getInstance(SessionSetting.class).getCsrfTokenHeader());
    String sessionCsrfToken = authInfo.getAuthToken().getCsrfToken();
    if (headerCsrfToken != null && Objects.equals(headerCsrfToken, sessionCsrfToken)) {
      return;
    }

    throw new AuthorizationException(insecureMessage);
  }
}
