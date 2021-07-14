package jp.co.freemind.calico.value.lang;

import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = LocalDateTime.class, factoryMethod = "of")
public class LocalDateTimeValue extends LangValue<LocalDateTime> {
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
    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
    .toFormatter()
    .withResolverStyle(ResolverStyle.STRICT)
    .withChronology(IsoChronology.INSTANCE)
    ;

  private static ValueConverter<LocalDateTime> converter = value ->
    LocalDateTime.parse(value, defaultFormatter);

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
