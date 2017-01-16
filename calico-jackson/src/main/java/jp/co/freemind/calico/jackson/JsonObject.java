package jp.co.freemind.calico.jackson;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.co.freemind.calico.jackson.ser.LocalDateSerializer;
import jp.co.freemind.calico.jackson.ser.LocalDateTimeSerializer;
import jp.co.freemind.calico.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalDateTimeDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalTimeDeserializer;
import jp.co.freemind.calico.jackson.ser.LocalTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@EqualsAndHashCode
public class JsonObject<T> {
  private static final ObjectMapper mapper = buildMapper();
  private final T object;

  @SneakyThrows
  public JsonObject(String json) {
    JavaType javaType = mapper.constructType(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    this.object = mapper.readValue(String.valueOf(json), javaType);
  }

  public JsonObject(T object) {
    this.object = object;
  }

  public T get() {
    return object;
  }

  public String getValue() {
    return toString();
  }

  @SneakyThrows
  public String toString() {
    return mapper.writeValueAsString(object);
  }

  private static ObjectMapper buildMapper() {
    ObjectMapper mapper = ObjectMapperProvider.createMapper();
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    SimpleModule module = new SimpleModule("DomaModule");
    module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
    module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
    mapper.registerModule(module);

    return mapper;
  }
}
