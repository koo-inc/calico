package jp.co.freemind.calico.core.endpoint.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.EndpointInfo;
import org.aopalliance.intercept.Invocation;

public class EndpointInvocation implements Invocation {
  private final EndpointInfo info;
  private final Object input;
  private final InterceptionHandler[] handlers;

  public EndpointInvocation(EndpointInfo info, Object input, InterceptionHandler[] handlers) {
    this.info = info;
    this.input = input;
    this.handlers = getMatchesHandlers(info, handlers);
  }

  private static Method getExecuteMethod(Class<? extends Endpoint> endpointClass) {
    try {
      return endpointClass.getMethod("execute", Object.class);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("unknown execute method");
    }
  }

  private static InterceptionHandler[] getMatchesHandlers(EndpointInfo info, InterceptionHandler[] handlers) {
    return Arrays.stream(handlers)
      .filter(h -> h.matches(info.getEndpointClass()))
      .toArray(InterceptionHandler[]::new);
  }

  public Object getInput() {
    return input;
  }

  @Override
  public Object[] getArguments() {
    return new Object[]{ input };
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object proceed() throws Throwable {
    if (handlers.length == 0) {
      try {
        return info.getExecuteMethod().invoke(getThis(), input);
      } catch (InvocationTargetException t) {
        throw t.getCause();
      }
    }
    EndpointInvocation next = new EndpointInvocation(info, input, Arrays.copyOfRange(handlers, 1, handlers.length));
    return handlers[0].invoke(next);
  }

  @Override
  public Object getThis() {
    return info.createInstance();
  }

  @Override
  public AccessibleObject getStaticPart() {
    return info.getExecuteMethod();
  }

  public Class<? extends Endpoint<?, ?>> getEndpointClass() {
    return info.getEndpointClass();
  }
}
