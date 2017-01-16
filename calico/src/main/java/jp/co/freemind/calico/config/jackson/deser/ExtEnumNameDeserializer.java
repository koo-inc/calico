package jp.co.freemind.calico.config.jackson.deser;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.google.common.collect.Maps;
import jp.co.freemind.calico.extenum.ExtEnum;
import lombok.SneakyThrows;

/**
 * Created by kakusuke on 15/07/30.
 */
@SuppressWarnings("rawtypes")
public class ExtEnumNameDeserializer extends JsonDeserializer<ExtEnum> implements ContextualDeserializer {
  @Override
  public ExtEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return null;
  }

  private static final ConcurrentMap<Class<ExtEnum>, EnumDeserializerInner> deserializers = Maps.newConcurrentMap();
  @Override
  @SuppressWarnings("unchecked")
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    return deserializers.computeIfAbsent((Class<ExtEnum>) property.getType().getRawClass(), EnumDeserializerInner::new);
  }

  private class EnumDeserializerInner extends JsonDeserializer<Object> {
    private final Class<?> rawClass;
    private final ExtEnum[] values;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    EnumDeserializerInner(Class<ExtEnum> rawClass) {
      this.rawClass = rawClass;
      this.values = ExtEnum.values(rawClass);
    }

    @Override
    public ExtEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      String name = p.getValueAsString();
      for (ExtEnum<?> e : values) {
        if (Objects.equals(e.getName(), name)) {
          return e;
        }
      }
      throw ctxt.mappingException(rawClass);
    }
  }
}
