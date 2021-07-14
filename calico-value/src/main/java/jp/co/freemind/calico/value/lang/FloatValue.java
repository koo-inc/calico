package jp.co.freemind.calico.value.lang;


import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

@Domain(valueType = Float.class, factoryMethod = "of")
public class FloatValue extends NumberValue<Float> {
  protected FloatValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<Float> getComponentClass() {
    return Float.class;
  }

  @Override
  public Float getValue() {
    return super.getValue();
  }

  @JsonCreator
  public static FloatValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new FloatValue(trim(rawValue));
  }
  public static FloatValue of(Float rawValue) {
    return of((Object)rawValue);
  }
}
