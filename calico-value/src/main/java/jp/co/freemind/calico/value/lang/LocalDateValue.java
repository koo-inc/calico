package jp.co.freemind.calico.value.lang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = LocalDate.class, factoryMethod = "of")
public class LocalDateValue extends LangValue<LocalDate> {
  private static ValueConverter<LocalDate> converter = value -> LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);

  protected LocalDateValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<LocalDate> getComponentClass() {
    return LocalDate.class;
  }

  @Override
  public LocalDate getValue() {
    return super.getValue();
  }

  @Override
  protected ValueConverter<LocalDate> getConverter() {
    return converter;
  }

  @JsonCreator
  public static LocalDateValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new LocalDateValue(trim(rawValue));
  }
  public static LocalDateValue of(LocalDate rawValue) {
    return of((Object)rawValue);
  }

  /** 設定処理は非スレッドセーフ */
  public static void setConverter(ValueConverter<LocalDate> converter) {
    LocalDateValue.converter = converter;
  }
}
