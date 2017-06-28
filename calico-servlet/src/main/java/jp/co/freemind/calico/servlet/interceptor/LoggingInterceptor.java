package jp.co.freemind.calico.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;

import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.Keys;

public class LoggingInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    HttpServletRequest req = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST);

    Zone.getCurrent().fork(s -> s
      .scope(TransactionScoped.class)
    ).run(() -> {

    });
    return null;
  }
}
