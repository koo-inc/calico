package jp.co.freemind.calico.value.lang;


import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

@Domain(valueType = Double.class, factoryMethod = "of")
public class DoubleValue extends NumberValue<Double> {
  protected DoubleValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<Double> getComponentClass() {
    return Double.class;
  }

  @Override
  public Double getValue() {
    return super.getValue();
  }

  @JsonCreator
  public static DoubleValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new DoubleValue(trim(rawValue));
  }
  public static DoubleValue of(Double rawValue) {
    return of((Object)rawValue);
  }
}
