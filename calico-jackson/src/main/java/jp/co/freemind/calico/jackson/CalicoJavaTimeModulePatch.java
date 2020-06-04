package jp.co.freemind.calico.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.module.SimpleModule;

import jp.co.freemind.calico.jackson.deser.YearMonthDeserializer;
import jp.co.freemind.calico.jackson.ser.LocalDateSerializer;
import jp.co.freemind.calico.jackson.ser.LocalDateTimeSerializer;
import jp.co.freemind.calico.jackson.ser.YearMonthSerializer;
import jp.co.freemind.calico.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalDateTimeDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalTimeDeserializer;
import jp.co.freemind.calico.jackson.ser.LocalTimeSerializer;

/**
 * Created by tasuku on 15/03/23.
 */
public class CalicoJavaTimeModulePatch extends SimpleModule {
  public CalicoJavaTimeModulePatch() {
    this(ZoneId.systemDefault());
  }
  public CalicoJavaTimeModulePatch(ZoneId zoneId) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(zoneId);

    addDeserializer(LocalTime.class,     new LocalTimeDeserializer(formatter));
    addDeserializer(LocalDate.class,     new LocalDateDeserializer(formatter));
    addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
    addDeserializer(YearMonth.class,     new YearMonthDeserializer(formatter));

    addSerializer(LocalTime.class,     new LocalTimeSerializer(formatter));
    addSerializer(LocalDate.class,     new LocalDateSerializer(formatter));
    addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
    addSerializer(YearMonth.class,     new YearMonthSerializer(formatter));
  }
}
