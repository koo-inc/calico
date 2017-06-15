package jp.co.freemind.calico.jackson.deser;

import static com.google.common.primitives.Primitives.wrap;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jp.co.freemind.calico.core.orm.Identifier;

public class IdentifierDeserializer extends JsonDeserializer<Identifier<?>> {

  private static final TypeResolver resolver = new TypeResolver();
  private final Class<Identifier<Object>> raw;
  private final JavaType javaType;

  @SuppressWarnings("unchecked")
  public IdentifierDeserializer(JavaType type) {
    this.raw = (Class<Identifier<Object>>) type.getRawClass();
    Class<?> elasedType = resolver.resolve(type.getRawClass()).typeParametersFor(Identifier.class).get(0).getErasedType();
    this.javaType = TypeFactory.defaultInstance().constructType(elasedType);
  }

  @Override
  public Identifier<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return getValue(ctxt, p.getCodec().readValue(p, javaType));
  }

  private Identifier<?> getValue(DeserializationContext ctxt, Object value) throws JsonMappingException {
    if (value == null) return null;
    try {
      Constructor<Identifier<Object>> constructor = raw.getConstructor(javaType.getRawClass());
      return constructor.newInstance(getConverter(value.getClass(), javaType.getRawClass()).apply(value));
    } catch (ReflectiveOperationException e) {
      throw ctxt.mappingException(raw.getSimpleName() + " doesn't have appropriate constructor for Identifier. " +
        "It needs `public " + raw.getSimpleName() + "(" + javaType.toString() + " id)`. " +
        "And any exception doesn't be thrown.");
    }
  }

  private static final Function<Object, Object> NOOP = v -> v;
  private static final Map<Class<?>, Map<Class<?>, Function<Object, Object>>> converters = new HashMap<>();
  static {
    converters.put(String.class, map((m) -> {
      m.put(String.class, NOOP);
      m.put(Integer.class, (v) -> Integer.parseInt((String) v));
      m.put(Long.class, (v) -> Long.parseLong((String) v));
      m.put(Boolean.class, (v) -> Boolean.parseBoolean((String) v));
    }));

    converters.put(Integer.class, map(m -> {
      m.put(String.class, String::valueOf);
      m.put(Integer.class, NOOP);
      m.put(Long.class, (v) -> ((Integer) v).longValue());
      m.put(Boolean.class, (v) -> ((Integer) v) != 0);
    }));

    converters.put(Long.class, map(m -> {
      m.put(String.class, String::valueOf);
      m.put(Integer.class, (v) -> ((Long) v).intValue());
      m.put(Long.class, NOOP);
      m.put(Boolean.class, (v) -> ((Long) v) != 0);
    }));

    converters.put(Long.class, map(m -> {
      m.put(String.class, String::valueOf);
      m.put(Integer.class, (v) -> ((Boolean) v) ? 1 : 0);
      m.put(Long.class, (v) -> ((Boolean) v) ? 1L : 0L);
      m.put(Boolean.class, NOOP);
    }));
  }
  private Function<Object, Object> getConverter(Class<?> from, Class<?> to) {
    return converters.getOrDefault(wrap(from), Collections.emptyMap()).getOrDefault(wrap(to), NOOP);
  }

  private static Map<Class<?>, Function<Object, Object>> map(Consumer<Map<Class<?>, Function<Object, Object>>> consumer) {
    Map<Class<?>, Function<Object, Object>> map = new HashMap<>();
    consumer.accept(map);
    return map;
  }

  public static class Key extends KeyDeserializer {

    private final IdentifierDeserializer deserializer;

    public Key(JavaType type) {
      this.deserializer = new IdentifierDeserializer(type);
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      return deserializer.getValue(ctxt, key);
    }
  }
}
