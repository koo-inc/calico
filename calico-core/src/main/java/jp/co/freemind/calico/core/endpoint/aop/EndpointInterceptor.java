package jp.co.freemind.calico.core.endpoint.aop;

import org.aopalliance.intercept.Interceptor;

public interface EndpointInterceptor extends Interceptor {
  Object invoke(EndpointInvocation invocation) throws Throwable;
}
