package jp.co.freemind.calico.json;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Created by tasuku on 15/04/30.
 */
public class JsonObjectDeserializer extends JsonDeserializer<JsonObject<?>> {

  private final Class<JsonObject<Object>> raw;
  private final JavaType javaType;

  @SuppressWarnings("unchecked")
  public JsonObjectDeserializer(JavaType type) {
    this.raw = (Class<JsonObject<Object>>) type.getRawClass();
    this.javaType = TypeFactory.defaultInstance().constructType(((ParameterizedType) raw.getGenericSuperclass()).getActualTypeArguments()[0]);
  }

  @Override
  public JsonObject<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    try {
      Constructor<JsonObject<Object>> constructor = raw.getConstructor(javaType.getRawClass());
      Object object = p.getCodec().readValue(p, javaType);
      return constructor.newInstance(object);
    } catch (ReflectiveOperationException e) {
      throw ctxt.mappingException(raw.getSimpleName() + " doesn't have appropriate constructor for JsonObject. " +
        "It needs `public " + raw.getSimpleName() + "(" + javaType.toString() + " object)`. " +
        "And any exception doesn't be thrown.");
    }
  }
}
