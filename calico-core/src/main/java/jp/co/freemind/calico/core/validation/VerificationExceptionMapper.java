package jp.co.freemind.calico.core.validation;

import static jp.co.freemind.calico.core.endpoint.result.ResultType.JSON;
import static jp.co.freemind.calico.core.endpoint.result.ResultType.invalid;

import java.util.concurrent.atomic.AtomicReference;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.exception.VerificationException;
import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.calico.core.zone.Zone;

public class VerificationExceptionMapper implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    AtomicReference<Object> result = new AtomicReference<>();
    Zone.getCurrent().fork(s -> s
      .onError(e -> {
        if (!(e instanceof VerificationException)) {
          throw Throwables.sneakyThrow(e);
        }

        Violation violation = ((VerificationException) e).getViolation();

        result.set(new Result(invalid(JSON), Zone.getContext(), violation));
      })
    ).run(()-> result.set(invocation.proceed()));
    return result.get();
  }
}
