package jp.co.freemind.calico.servlet.util;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.emptyToNull;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.session.SessionSetting;

public final class SessionUtil {
  private SessionUtil() {}

  public static Optional<String> getSessionToken(HttpServletRequest req) {
    if(req.getCookies() == null) return Optional.empty();
    for (Cookie cookie : req.getCookies()) {
      if (cookie.getName().equals(getSetting().getTokenKey())) {
        return Optional.ofNullable(cookie.getValue());
      }
    }
    return Optional.empty();
  }

  public static void setSessionToken(ServletContext context, HttpServletResponse res, AuthToken token) {
    Cookie cookie = new Cookie(getSetting().getTokenKey(), token.getValue());
    cookie.setHttpOnly(true);
    cookie.setPath(getContextPath(context));
    res.addCookie(cookie);
  }

  public static void setXsrfToken(ServletContext context, HttpServletResponse res, AuthToken token) {
    Cookie cookie = new Cookie(getSetting().getCsrfTokenKey(), token.getCsrfToken());
    cookie.setHttpOnly(false);
    cookie.setPath(getContextPath(context));
    res.addCookie(cookie);
  }

  private static SessionSetting getSetting() {
    return Zone.getRoot().getInstance(SessionSetting.class);
  }

  private static String getContextPath(ServletContext context) {
    return firstNonNull(emptyToNull(context.getContextPath()), "/");
  }
}
