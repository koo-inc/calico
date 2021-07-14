package jp.co.freemind.calico.value.lang;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

@Domain(valueType = Integer.class, factoryMethod = "of")
public class IntegerValue extends NumberValue<Integer> {
  protected IntegerValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<Integer> getComponentClass() {
    return Integer.class;
  }

  @Override
  public Integer getValue() {
    return super.getValue();
  }

  @JsonCreator
  public static IntegerValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new IntegerValue(trim(rawValue));
  }
  public static IntegerValue of(Integer rawValue) {
    return of((Object)rawValue);
  }
}
