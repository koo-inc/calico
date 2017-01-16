package jp.co.freemind.calico.config.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.module.SimpleModule;

import jp.co.freemind.calico.config.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.config.jackson.deser.LocalDateTimeDeserializer;
import jp.co.freemind.calico.config.jackson.deser.LocalTimeDeserializer;
import jp.co.freemind.calico.config.jackson.deser.YearMonthDeserializer;
import jp.co.freemind.calico.config.jackson.ser.LocalDateSerializer;
import jp.co.freemind.calico.config.jackson.ser.LocalDateTimeSerializer;
import jp.co.freemind.calico.config.jackson.ser.LocalTimeSerializer;
import jp.co.freemind.calico.config.jackson.ser.YearMonthSerializer;

/**
 * Created by tasuku on 15/03/23.
 */
public class CalicoJavaTimeModulePatch extends SimpleModule {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());

  public CalicoJavaTimeModulePatch() {
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
