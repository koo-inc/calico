package jp.co.freemind.calico.service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import javax.validation.Valid;

import jp.co.freemind.calico.dto.Form;
import jp.co.freemind.calico.service.exception.ServiceException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ServiceInvokeInterceptor implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    for(int i = 0;i < invocation.getArguments().length;i++){
      Object arg = invocation.getArguments()[i];
      Parameter param = invocation.getMethod().getParameters()[i];
      if (param.getAnnotation(Valid.class) == null) continue;
      if (!(arg instanceof Form)) continue;

      Map<String, String> violations = ((Form) arg).validate();
      if (!violations.isEmpty()) {
        ServiceException e = new ServiceException();
        violations.forEach(e::add);
        throw e;
      }
    }
    return invocation.proceed();
  }

  private Method getValidateMethod(Class<?> type) {
    Class<?> current = type;
    while(current != Object.class) {
      for (Method method : current.getDeclaredMethods()) {
        if (!method.getName().equals("validateWithExceptionThrow")) continue;
        if (method.getParameterCount() > 0) continue;
        return method;
      }
      current = current.getSuperclass();
    }
    throw new IllegalStateException(type.getSimpleName() + " に validateWithExceptionThrow が定義されていません");
  }
}
