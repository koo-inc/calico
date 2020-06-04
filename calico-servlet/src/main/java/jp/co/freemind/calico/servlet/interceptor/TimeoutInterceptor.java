package jp.co.freemind.calico.servlet.interceptor;

import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.di.Context;
import jp.co.freemind.calico.core.di.InjectorRef;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.servlet.Keys;
import jp.co.freemind.calico.servlet.SessionSetting;

public class TimeoutInterceptor implements EndpointInterceptor {
  private final Supplier<AuthInfo> nullAuthInfoSupplier;

  public TimeoutInterceptor(@Nonnull Supplier<AuthInfo> nullAuthInfoSupplier) {
    this.nullAuthInfoSupplier = nullAuthInfoSupplier;
  }

  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    if (!InjectorRef.getContext().getAuthInfo().isAuthenticated()) {
      return invocation.proceed();
    }

    long timeout = InjectorRef.getInstance(SessionSetting.class).timeoutSecond();

    if (timeout > 0) {
      AuthToken authToken = InjectorRef.getInstance(Keys.AUTH_TOKEN);
      Context context = InjectorRef.getContext();
      if (context.getProcessDateTime().minus(timeout, ChronoUnit.SECONDS).compareTo(authToken.getCreatedAt()) > 0) {

        AuthInfo authInfo = this.nullAuthInfoSupplier.get();
        context.setAuthInfo(authInfo);
        Object result = invocation.proceed();

        if (result instanceof Result) {
          return result;
        }
        else {
          return new Result(context, result);
        }
      }
    }

    return invocation.proceed();
  }
}
