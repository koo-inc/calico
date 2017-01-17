package jp.co.freemind.calico.endpoint.interceptor;

import java.util.Comparator;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.common.collect.Maps;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.service.exception.ServiceException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class EndpointValidationInterceptor implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object input = invocation.getArguments()[0];
    Map<String, String> violations = validate(input);
    if (!violations.isEmpty()) {
      ServiceException e = new ServiceException();
      violations.forEach(e::add);
      throw e;
    }
    return invocation.proceed();
  }

  private Map<String, String> validate(Object input){
    Validator validator = InjectUtil.getInstance(Validator.class);
    Map<String, String> violations = Maps.newTreeMap(Comparator.<String>naturalOrder());
    for (ConstraintViolation<Object> violation : validator.validate(input)) {
      violations.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return violations;
  }
}
