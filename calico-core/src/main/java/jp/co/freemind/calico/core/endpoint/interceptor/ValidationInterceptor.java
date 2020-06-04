package jp.co.freemind.calico.core.endpoint.interceptor;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.validation.Validator;
import jp.co.freemind.calico.core.exception.InvalidUserDataException;
import jp.co.freemind.calico.core.validation.Violation;
import jp.co.freemind.calico.core.di.InjectorRef;

public class ValidationInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    Violation violation = validate(invocation);
    if (violation.isFound()) {
      throw new InvalidUserDataException(violation);
    }
    return invocation.proceed();
  }

  private Violation validate(EndpointInvocation invocation){
    Object input = invocation.getInput();
    if (input == null) return new Violation();
    return getValidator().validate(input.getClass(), input);
  }

  private Validator getValidator() {
    return InjectorRef.getInstance(Validator.class);
  }
}
