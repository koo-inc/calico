package jp.co.freemind.calico.value.lang;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = LocalTime.class, factoryMethod = "of")
public class LocalTimeValue extends LangValue<LocalTime> {
  private static ValueConverter<LocalTime> converter = value -> LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);

  protected LocalTimeValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<LocalTime> getComponentClass() {
    return LocalTime.class;
  }

  @Override
  public LocalTime getValue() {
    return super.getValue();
  }

  @Override
  protected ValueConverter<LocalTime> getConverter() {
    return converter;
  }

  @JsonCreator
  public static LocalTimeValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new LocalTimeValue(trim(rawValue));
  }
  public static LocalTimeValue of(LocalTime rawValue) {
    return of((Object)rawValue);
  }

  /** 設定処理は非スレッドセーフ */
  public static void setConverter(ValueConverter<LocalTime> converter) {
    LocalTimeValue.converter = converter;
  }
}
