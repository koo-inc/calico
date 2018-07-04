package jp.co.freemind.calico.jackson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.calico.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalDateTimeDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalTimeDeserializer;
import jp.co.freemind.calico.jackson.ser.LocalDateSerializer;
import jp.co.freemind.calico.jackson.ser.LocalDateTimeSerializer;
import jp.co.freemind.calico.jackson.ser.LocalTimeSerializer;

public class JsonObject<T> {
  private static final ObjectMapper mapper = buildMapper();
  private final T object;

  public JsonObject(String json) {
    JavaType javaType = mapper.constructType(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    try {
      this.object = mapper.readValue(String.valueOf(json), javaType);
    } catch (IOException e) {
      throw Throwables.sneakyThrow(e);
    }
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

  public String toString() {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw Throwables.sneakyThrow(e);
    }
  }

  private static ObjectMapper buildMapper() {
    SimpleModule module = new SimpleModule("DomaModule");
    module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
    module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));

    ObjectMapper mapper = ObjectMapperProvider.createMapper(module);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    return mapper;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof JsonObject)) return false;
    final JsonObject other = (JsonObject) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$object = this.object;
    final Object other$object = other.object;
    if (this$object == null ? other$object != null : !this$object.equals(other$object)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $object = this.object;
    result = result * PRIME + ($object == null ? 43 : $object.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof JsonObject;
  }
}
