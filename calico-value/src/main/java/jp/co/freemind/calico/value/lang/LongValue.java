package jp.co.freemind.calico.value.lang;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

@Domain(valueType = Long.class, factoryMethod = "of")
public class LongValue extends NumberValue<Long> {
  protected LongValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<Long> getComponentClass() {
    return Long.class;
  }

  @Override
  public Long getValue() {
    return super.getValue();
  }

  @JsonCreator
  public static LongValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new LongValue(trim(rawValue));
  }
  public static LongValue of(Long rawValue) {
    return of((Object)rawValue);
  }
}
