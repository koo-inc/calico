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
import jp.co.freemind.calico.servlet.util.CookieUtil;

public class CsrfInterceptor implements EndpointInterceptor {
  private final Message insecureMessage;

  public CsrfInterceptor(Message insecureMessage) {
    this.insecureMessage = insecureMessage;
  }

  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    HttpServletRequest req = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST);
    AuthInfo authInfo = Zone.getContext().getAuthInfo();

    if (authInfo.isUsedBySystem()) {
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
