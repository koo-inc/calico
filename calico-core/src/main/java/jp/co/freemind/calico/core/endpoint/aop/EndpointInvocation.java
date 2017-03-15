package jp.co.freemind.calico.core.endpoint.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.EndpointResolver;
import org.aopalliance.intercept.Invocation;

public class EndpointInvocation implements Invocation {
  private final EndpointResolver resolver;
  private final Class<? extends Endpoint<?, ?>> endpointClass;
  private final Method method;
  private final Object input;
  private final InterceptionHandler[] handlers;

  public EndpointInvocation(EndpointResolver resolver, Class<? extends Endpoint<?, ?>> endpointClass, Object input, InterceptionHandler[] handlers) {
    this(resolver, endpointClass, getExecuteMethod(endpointClass), input, getMatchesHandlers(endpointClass, handlers));
  }
  private EndpointInvocation(EndpointResolver resolver, Class<? extends Endpoint<?, ?>> endpointClass, Method method, Object input, InterceptionHandler[] handlers) {
    this.resolver = resolver;
    this.endpointClass = endpointClass;
    this.input = input;
    this.method = method;
    this.handlers = handlers;
  }

  private static Method getExecuteMethod(Class<? extends Endpoint> endpointClass) {
    try {
      return endpointClass.getMethod("execute", Object.class);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("unknown execute method");
    }
  }

  private static InterceptionHandler[] getMatchesHandlers(Class<? extends Endpoint> endpointClass, InterceptionHandler[] handlers) {
    return Arrays.stream(handlers)
      .filter(h -> h.matches(endpointClass))
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
      return method.invoke(getThis(), input);
    }
    EndpointInvocation next = new EndpointInvocation(resolver, endpointClass, method, input, Arrays.copyOfRange(handlers, 1, handlers.length));
    return handlers[0].invoke(next);
  }

  @Override
  public Object getThis() {
    return resolver.getEndpoint(endpointClass);
  }

  @Override
  public AccessibleObject getStaticPart() {
    return method;
  }

  public Class<? extends Endpoint<?, ?>> getEndpointClass() {
    return endpointClass;
  }
}
