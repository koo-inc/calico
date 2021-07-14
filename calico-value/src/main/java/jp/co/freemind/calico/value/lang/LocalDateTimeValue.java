package jp.co.freemind.calico.value.lang;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = LocalDateTime.class, factoryMethod = "of")
public class LocalDateTimeValue extends LangValue<LocalDateTime> {
  private static ValueConverter<LocalDateTime> converter = value -> LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

  protected LocalDateTimeValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<LocalDateTime> getComponentClass() {
    return LocalDateTime.class;
  }

  @Override
  public LocalDateTime getValue() {
    return super.getValue();
  }

  @Override
  protected ValueConverter<LocalDateTime> getConverter() throws DateTimeParseException {
    return converter;
  }

  @JsonCreator
  public static LocalDateTimeValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new LocalDateTimeValue(trim(rawValue));
  }
  public static LocalDateTimeValue of(LocalDateTime rawValue) {
    return of((Object)rawValue);
  }

  /** 設定処理は非スレッドセーフ */
  public static void setConverter(ValueConverter<LocalDateTime> converter) {
    LocalDateTimeValue.converter = converter;
  }
}
