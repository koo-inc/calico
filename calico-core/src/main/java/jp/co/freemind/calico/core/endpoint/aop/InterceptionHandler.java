package jp.co.freemind.calico.core.endpoint.aop;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.endpoint.Endpoint;

public class InterceptionHandler {
  private final Matcher<Class> matcher;
  private final EndpointInterceptor interceptor;

  public InterceptionHandler(Matcher<Class> matcher, EndpointInterceptor interceptor) {
    this.matcher = matcher;
    this.interceptor = interceptor;
  }
  @SuppressWarnings("unchecked")
  public InterceptionHandler(EndpointInterceptor interceptor) {
    this((Matcher<Class>) (Matcher) Matchers.any(), interceptor);
  }

  public boolean matches(Class<? extends Endpoint> endpointClass) {
    return matcher.matches(endpointClass);
  }

  public Object invoke(EndpointInvocation invocation) throws Throwable {
    return interceptor.invoke(invocation);
  }

  public Class<? extends EndpointInterceptor> getInterceptorClass() {
    return this.interceptor.getClass();
  }
}
