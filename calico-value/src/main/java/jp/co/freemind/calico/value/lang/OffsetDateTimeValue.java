package jp.co.freemind.calico.value.lang;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.seasar.doma.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.freemind.calico.value.ValueConverter;

@Domain(valueType = Timestamp.class, factoryMethod = "of", accessorMethod = "toTimestamp")
public class OffsetDateTimeValue extends LangValue<OffsetDateTime> {
  private static ValueConverter<OffsetDateTime> converter = value -> OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  private static ZoneId zoneId = ZoneId.systemDefault();

  protected OffsetDateTimeValue(Object rawValue) {
    super(rawValue);
  }

  @Override
  protected Class<OffsetDateTime> getComponentClass() {
    return OffsetDateTime.class;
  }

  @Override
  public OffsetDateTime getValue() {
    return super.getValue();
  }

  @Override
  protected ValueConverter<OffsetDateTime> getConverter() throws DateTimeParseException {
    return converter;
  }

  public Timestamp toTimestamp() {
    return Timestamp.from(getValue().toInstant());
  }

  @JsonCreator
  public static OffsetDateTimeValue of(Object rawValue) {
    if (isBlank(rawValue)) return null;
    return new OffsetDateTimeValue(trim(rawValue));
  }
  public static OffsetDateTimeValue of(LocalDateTime rawValue) {
    return of((Object)rawValue);
  }
  public static OffsetDateTimeValue of(Timestamp timestamp) {
    Instant instant = timestamp.toInstant();
    OffsetDateTime dateTime = instant.atOffset(zoneId.getRules().getOffset(instant));
    return of(dateTime);
  }

  /** 設定処理は非スレッドセーフ */
  public static void setConverter(ValueConverter<OffsetDateTime> converter) {
    OffsetDateTimeValue.converter = converter;
  }
  /** 設定処理は非スレッドセーフ */
  public static void setZoneId(ZoneId zoneId) {
    OffsetDateTimeValue.zoneId = zoneId;
  }
}
