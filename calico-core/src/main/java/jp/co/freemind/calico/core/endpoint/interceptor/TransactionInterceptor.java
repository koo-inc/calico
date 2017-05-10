package jp.co.freemind.calico.core.endpoint.interceptor;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.SneakyThrows;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class TransactionInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    TransactionManager tm = Zone.getCurrent().getInstance(Config.class).getTransactionManager();
    return tm.requiresNew(TransactionIsolationLevel.READ_COMMITTED, () -> proceed(invocation));
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private <T> T proceed(EndpointInvocation invocation) {
    return (T) invocation.proceed();
  }
}
