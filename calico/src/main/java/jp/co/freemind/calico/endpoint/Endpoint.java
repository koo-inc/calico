package jp.co.freemind.calico.endpoint;

import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.Getter;

@Getter
public abstract class Endpoint<INPUT, OUTPUT> {
  private Method executeMethod;
  private Class<?> inputClass;
  private Class<?> outputClass;

  public Endpoint(){
    executeMethod = Arrays.stream(this.getClass().getMethods())
      .filter(m ->  m.getName().equals("execute"))
      .filter(m -> !(m.getParameterTypes()[0] == Object.class && m.getReturnType() == Object.class)) //内部的に (Object) -> Object なexecuteが生成される場合がある
      .findFirst().orElseGet(null);
    inputClass = executeMethod.getParameterTypes()[0];
    outputClass = executeMethod.getReturnType();
  }

  public abstract OUTPUT execute(INPUT form);
}
