package jp.co.freemind.calico.core.endpoint;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import jp.co.freemind.calico.core.di.InjectorRef;

@Immutable
public class EndpointInfo {
  @Nonnull
  final private Class<? extends Endpoint<?, ?>> endpointClass;
  @Nonnull
  final private Class<?> inputType;
  @Nonnull
  final private Class<?> outputType;
  @Nonnull
  final private Method executeMethod;

  EndpointInfo(Class<? extends Endpoint<?, ?>> endpointClass) {
    this.endpointClass = Objects.requireNonNull(endpointClass);
    this.executeMethod = getExecuteMethod(endpointClass);
    this.inputType = (Class<?>) executeMethod.getParameters()[0].getParameterizedType();
    this.outputType = executeMethod.getReturnType();
  }

  public Class<? extends Endpoint<?, ?>> getEndpointClass() { return endpointClass; }
  public Class<?> getInputType() { return inputType; }
  public Class<?> getOutputType() { return outputType; }

  public Object createInstance() {
    return InjectorRef.getInstance(endpointClass);
  }

  public Method getExecuteMethod() {
    return executeMethod;
  }

  private static Method getExecuteMethod(Class<? extends Endpoint<?, ?>> endpointClass) {
    return Arrays.stream(endpointClass.getMethods())
      .filter(m ->  m.getName().equals("execute"))
      .filter(m -> !(m.getParameterTypes()[0] == Object.class && m.getReturnType() == Object.class)) //内部的に (Object) -> Object なexecuteが生成される場合がある
      .findFirst()
      .orElseThrow(NullPointerException::new);
  }
}
