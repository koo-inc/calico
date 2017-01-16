package calicosample.core.security;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;

import calicosample.core.auth.CalicoSampleAuthInfo;
import org.apache.commons.lang3.StringUtils;
import jp.co.freemind.calico.context.Context;
import calicosample.core.auth.CsrfException;
import calicosample.core.auth.CsrfGuard;
import calicosample.core.auth.NoAuth;
import calicosample.core.auth.NoCsrfGuard;
import jp.co.freemind.calico.auth.AuthToken;

@Priority(Priorities.AUTHORIZATION + 1)
public class SecurityFilter implements ContainerRequestFilter, ContainerResponseFilter {
  public static final String CSRF_TOKEN_KEY =  "X-XSRF-TOKEN";
  public static final String CSRF_TOKEN_COOKIE = "XSRF-TOKEN";

  @javax.ws.rs.core.Context
  private HttpServletRequest request;
  @javax.ws.rs.core.Context
  private HttpServletResponse response;
  @javax.ws.rs.core.Context
  private ResourceInfo resourceInfo;
  @javax.ws.rs.core.Context
  private Configuration configuration;
  @Inject private javax.inject.Provider<Context<CalicoSampleAuthInfo>> contextProvider;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    if(!isAuthenticationRequired()) return;

    AuthToken authToken = contextProvider.get().getAuthToken();
    validateAuthTokenWhenPost(authToken);
    validateAuthTokenWhenGet(authToken);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    AuthToken authToken = contextProvider.get().getAuthToken();

    Cookie csrfCookie = new Cookie(CSRF_TOKEN_COOKIE, authToken.getCsrfToken());
    csrfCookie.setPath(StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath());
    csrfCookie.setHttpOnly(false);
    response.addCookie(csrfCookie);

    // http://d.hatena.ne.jp/sen-u/20131130/p1
    response.addHeader("X-Frame-Options", "SAMEORIGIN");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
    // 適用できなかったため
    // response.addHeader("Content-Security-Policy", "default-src 'self'");
    // HTTPSのときだけ有効化する?
    // if (Env.getEnvName().equals("production")) response.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
  }

  private <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
    T anno = resourceInfo.getResourceClass().getAnnotation(annotationClass);
    if (anno != null) return true;
    return resourceInfo.getResourceMethod().getAnnotation(annotationClass) != null;
  }

  private boolean isAuthenticationRequired(){
    return !isAnnotationPresent(NoAuth.class);
  }

  private void validateAuthTokenWhenPost(AuthToken authToken) {
    if (!request.getMethod().equals("POST")) return;
    if (isAnnotationPresent(NoCsrfGuard.class)) return;
    validateAuthToken(authToken);
  }

  private void validateAuthTokenWhenGet(AuthToken authToken) {
    if (!request.getMethod().equals("GET")) return;
    if (!isAnnotationPresent(CsrfGuard.class)) return;
    validateAuthToken(authToken);
  }

  private void validateAuthToken(AuthToken authToken) {
    // CSRFトークンチェック
    if (!authToken.isValidCsrfToken(request.getHeader(CSRF_TOKEN_KEY))) {
      throw new CsrfException();
    }
  }
}
