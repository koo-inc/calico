package calicosample.core.auth;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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

import org.apache.commons.lang3.StringUtils;
import jp.co.freemind.calico.context.Context;
import calicosample.core.WebContext;
import jp.co.freemind.calico.auth.AuthToken;

@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {
  @javax.ws.rs.core.Context
  private HttpServletRequest request;
  @javax.ws.rs.core.Context
  private HttpServletResponse response;
  @javax.ws.rs.core.Context
  private ResourceInfo resourceInfo;
  @Inject private javax.inject.Provider<Context<CalicoSampleAuthInfo>> contextProvider;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    if(!isAuthenticationRequired()) return;

    Context<CalicoSampleAuthInfo> context = contextProvider.get();
    if (!context.getAuthInfo().isAuthenticated()) {
      throw authError();
    }
     // IPチェック
    if (!context.getAuthToken().isValidRemoteAddress(context.getRemoteAddress())) {
      throw authError();
    }
    // タイムアウトチェック
    if (context.getAuthToken().hasBeenCreatedFor(Duration.of(8, ChronoUnit.HOURS))) {
      throw authError();
    }
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    AuthToken authToken = contextProvider.get().getAuthToken().regenerate();
    Cookie cookie = new Cookie(WebContext.SESSION_TOKEN_COOKIE, authToken.getValue());
    cookie.setPath(StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath());
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  private <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
    T anno = resourceInfo.getResourceClass().getAnnotation(annotationClass);
    if (anno != null) return true;
    return resourceInfo.getResourceMethod().getAnnotation(annotationClass) != null;
  }

  private boolean isAuthenticationRequired(){
    return !isAnnotationPresent(NoAuth.class);
  }

  private AuthenticationRequiredException authError() throws AuthenticationRequiredException {
    Context<CalicoSampleAuthInfo> context = contextProvider.get();
    Context.Builder<CalicoSampleAuthInfo> builder = Context.builder();
    context.switchTo(builder
      .inheritFrom(context)
      .authInfo(CalicoSampleAuthInfo.ofNull())
      .authToken(AuthToken.createEmpty(context.getRemoteAddress(), context.getProcessDateTime()))
      .build());
    throw new AuthenticationRequiredException();
  }
}
