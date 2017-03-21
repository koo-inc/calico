package jp.co.freemind.calico.servlet.session;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.Keys;

public class TimeoutInterceptor implements EndpointInterceptor {
  private final Supplier<AuthInfo> nullAuthInfoSupplier;

  public TimeoutInterceptor(@Nonnull Supplier<AuthInfo> nullAuthInfoSupplier) {
    this.nullAuthInfoSupplier = nullAuthInfoSupplier;
  }

  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    long timeout = Zone.getCurrent().getInstance(SessionSetting.class).timeoutSecond();

    if (timeout > 0) {
      AuthToken authToken = Zone.getCurrent().getInstance(Keys.AUTH_TOKEN);
      if (LocalDateTime.now().minus(timeout, ChronoUnit.SECONDS).compareTo(authToken.getCreatedAt()) > 0) {
        Context context = Zone.getContext();

        AuthInfo authInfo = this.nullAuthInfoSupplier.get();
        Context newContext = context.extend(c -> c
          .authInfo(authInfo)
        );

        AtomicReference<Object> ref = new AtomicReference<>();
        Zone.getCurrent().fork(s -> s
          .scope(TransactionScoped.class)
          .provide(newContext)
        ).run(()-> ref.set(invocation.proceed()));

        Object result = ref.get();
        if (result instanceof Result) {
          return result;
        }
        else {
          return new Result(newContext, result);
        }
      }
    }

    return invocation.proceed();
  }
}
