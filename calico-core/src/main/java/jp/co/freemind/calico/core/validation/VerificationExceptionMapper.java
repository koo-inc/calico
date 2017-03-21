package jp.co.freemind.calico.core.validation;

import static jp.co.freemind.calico.core.endpoint.result.ResultType.*;

import java.util.concurrent.atomic.AtomicReference;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.exception.InconsistentDataException;
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

        if (e instanceof InconsistentDataException) {
          result.set(new Result(conflict(JSON), Zone.getContext(), violation));
        }
        else if (e instanceof AuthorizationException) {
          result.set(new Result(forbidden(JSON), Zone.getContext(), violation));
        }
        else {
          result.set(new Result(invalid(JSON), Zone.getContext(), violation));
        }
      })
    ).run(()-> result.set(invocation.proceed()));
    return result.get();
  }
}
