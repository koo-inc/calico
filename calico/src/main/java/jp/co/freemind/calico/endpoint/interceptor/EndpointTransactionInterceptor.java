package jp.co.freemind.calico.endpoint.interceptor;

import jp.co.freemind.calico.guice.InjectUtil;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class EndpointTransactionInterceptor implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    TransactionManager tm = InjectUtil.getInstance(Config.class).getTransactionManager();
    return tm.required(TransactionIsolationLevel.SERIALIZABLE, () -> proceed(invocation));
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private <T> T proceed(MethodInvocation invocation) {
    T result = (T) invocation.proceed();
    return result;
  }
}
