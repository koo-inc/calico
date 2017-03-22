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
import jp.co.freemind.calico.servlet.SessionSetting;

public final class CookieUtil {
  private CookieUtil() {}

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
    res.addCookie(generateCookie(context, getSetting().getTokenKey(), token.getValue(), true));
  }

  public static void setXsrfToken(ServletContext context, HttpServletResponse res, AuthToken token) {
    res.addCookie(generateCookie(context, getSetting().getCsrfTokenKey(), token.getCsrfToken(), false));
  }

  public static Cookie generateCookie(ServletContext context, String key, Object value, boolean httpOnly) {
    Cookie cookie = new Cookie(key, String.valueOf(value));
    cookie.setPath(getContextPath(context));
    cookie.setHttpOnly(httpOnly);
    return cookie;
  }

  private static SessionSetting getSetting() {
    return Zone.getRoot().getInstance(SessionSetting.class);
  }

  private static String getContextPath(ServletContext context) {
    return firstNonNull(emptyToNull(context.getContextPath()), "/");
  }
}
