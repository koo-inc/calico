package calicosample.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.co.freemind.calico.config.jackson.ObjectMapperProvider;
import jp.co.freemind.calico.config.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.config.jackson.deser.LocalDateTimeDeserializer;
import jp.co.freemind.calico.config.jackson.deser.LocalTimeDeserializer;
import jp.co.freemind.calico.config.jackson.ser.LocalDateSerializer;
import jp.co.freemind.calico.config.jackson.ser.LocalDateTimeSerializer;
import jp.co.freemind.calico.config.jackson.ser.LocalTimeSerializer;
import jp.co.freemind.csv.CsvFormatter;
import jp.co.freemind.csv.CsvMapper;

/**
 * Created by kakusuke on 15/07/29.
 */
public class CsvUtil {
  private static final ObjectMapper mapper = ObjectMapperProvider.createMapper(new CsvModule());

  public static <T> CsvMapper<T> createMapper(Class<T> targetClass) {
    return createMapper(targetClass, targetClass);
  }
  public static <T> CsvMapper<T> createMapper(Class<T> targetClass, Class<?> formatClass) {
    return createMapper(CsvFormatter.builder(targetClass).with(formatClass).withHeaders().build());
  }
  public static <T> CsvMapper<T> createMapper(CsvFormatter<T> formatter) {
    return new CsvMapper<>(formatter, mapper);
  }

  public static <T> CsvMapper<T> createMapperForDownload(Class<T> targetClass) {
    return createMapperForDownload(targetClass, targetClass);
  }
  public static <T> CsvMapper<T> createMapperForDownload(Class<T> targetClass, Class<?> formatClass) {
    return createMapper(CsvFormatter
      .builder(targetClass).with(formatClass)
      .charset("UTF-8").withBom()
      .withHeaders()
      .build());
  }

  public static <T> CsvMapper<T> createMapperForSystem(Class<T> targetClass) {
    return createMapperForSystem(targetClass, targetClass);
  }
  public static <T> CsvMapper<T> createMapperForSystem(Class<T> targetClass, Class<?> formatClass) {
    return createMapper(CsvFormatter
      .builder(targetClass).with(formatClass)
      .charset("UTF-8").withoutBom()
      .withoutHeader()
      .build());
  }

  public static <T> CsvMapper<T> createMapperForLegacySystem(Class<T> targetClass) {
    return createMapperForLegacySystem(targetClass, targetClass);
  }
  public static <T> CsvMapper<T> createMapperForLegacySystem(Class<T> targetClass, Class<?> formatClass) {
    return createMapper(CsvFormatter
      .builder(targetClass).with(formatClass)
      .charset("MS932")
      .withoutHeader()
      .build());
  }

  private static class CsvModule extends SimpleModule {
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d H:m:s").withResolverStyle(ResolverStyle.LENIENT);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d").withResolverStyle(ResolverStyle.LENIENT);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:m:s").withResolverStyle(ResolverStyle.LENIENT);

    CsvModule() {
      super("CSV Module");
      addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMAT));
      addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMAT));

      addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT));
      addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMAT));

      addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMAT));
      addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMAT));
    }
  }
}
