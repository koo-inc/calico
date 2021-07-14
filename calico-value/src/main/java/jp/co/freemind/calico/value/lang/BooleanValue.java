package jp.co.freemind.calico.value.lang;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = Boolean.class, factoryMethod = "of")
public class BooleanValue extends LangValue<Boolean> {
  protected BooleanValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<Boolean> getComponentClass() {
    return Boolean.class;
  }

  @Override
  public Boolean getValue() {
    return super.getValue();
  }

  @Override
  protected ValueConverter<Boolean> getConverter() {
    return value -> {
      switch (value) {
        case "true":
          return true;
        case "false":
          return false;
        default:
          throw new IllegalStateException(value + " is not boolean");
      }
    };
  }

  @JsonCreator
  public static BooleanValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new BooleanValue(trim(rawValue));
  }
  public static BooleanValue of(Boolean rawValue) {
    return of((Object)rawValue);
  }
}
