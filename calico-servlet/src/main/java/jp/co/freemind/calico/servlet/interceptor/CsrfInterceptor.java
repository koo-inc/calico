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
import jp.co.freemind.calico.servlet.util.CookieUtil;

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

    verifyCsrfToken(req);

    return invocation.proceed();
  }

  private void verifyCsrfToken(HttpServletRequest req) {
    String headerCsrfToken = req.getHeader(Zone.getCurrent().getInstance(SessionSetting.class).getCsrfTokenHeader());
    String cookieCsrfToken = CookieUtil.getCsrfToken(req).orElse(null);
    if (headerCsrfToken != null && Objects.equals(headerCsrfToken, cookieCsrfToken)) {
      return;
    }

    throw new AuthorizationException(insecureMessage);
  }
}
