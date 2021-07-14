package jp.co.freemind.calico.value.lang;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = LocalDate.class, factoryMethod = "of")
public class LocalDateValue extends LangValue<LocalDate> {
  private static final DateTimeFormatter defaultFormatter = new DateTimeFormatterBuilder()
    .appendValue(ChronoField.YEAR, 4, 10,SignStyle.EXCEEDS_PAD)
    .optionalStart().appendLiteral('-').optionalEnd()
    .optionalStart().appendLiteral('/').optionalEnd()
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .optionalStart().appendLiteral('-').optionalEnd()
    .optionalStart().appendLiteral('/').optionalEnd()
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .optionalStart()
    .appendLiteral('T')
    .append(DateTimeFormatter.ISO_LOCAL_TIME)
    .optionalStart().appendOffsetId().optionalEnd()
    .optionalEnd()
    .toFormatter()
    .withResolverStyle(ResolverStyle.STRICT)
    .withChronology(IsoChronology.INSTANCE);

  private static ValueConverter<LocalDate> converter = value -> LocalDate.parse(value, defaultFormatter);

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
