package jp.co.freemind.calico.servlet.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.Keys;
import jp.co.freemind.calico.servlet.util.CookieUtil;

public class VersioningInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    HttpServletRequest req = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST);
    HttpServletResponse res = Zone.getCurrent().getInstance(Keys.SERVLET_RESPONSE);
    setVersionTag(req, res);
    return invocation.proceed();
  }

  private void setVersionTag(HttpServletRequest req, HttpServletResponse res) {
    Cookie cookie = CookieUtil.generateCookie(req.getServletContext(), getVersionTag(), getVersion(), false);
    res.addCookie(cookie);
  }

  private long getVersion() {
    return Zone.getRoot().getInstance(SystemSetting.class).version();
  }

  private String getVersionTag() {
    return Zone.getRoot().getInstance(SystemSetting.class).getVersionTag();
  }
}
