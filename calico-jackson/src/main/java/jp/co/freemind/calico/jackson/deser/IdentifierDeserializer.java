package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jp.co.freemind.calico.core.orm.Identifier;

public class IdentifierDeserializer extends JsonDeserializer<Identifier<?>> {

  private final TypeResolver resolver = new TypeResolver();
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

  @Override
  public Identifier<?> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
    return getValue(ctxt, null);
  }

  private Identifier<?> getValue(DeserializationContext ctxt, @Nullable Object value) throws JsonMappingException {
    try {
      Constructor<Identifier<Object>> constructor = raw.getConstructor(javaType.getRawClass());
      return constructor.newInstance(value);
    } catch (ReflectiveOperationException e) {
      throw ctxt.mappingException(raw.getSimpleName() + " doesn't have appropriate constructor for Identifier. " +
        "It needs `public " + raw.getSimpleName() + "(" + javaType.toString() + " id)`. " +
        "And any exception doesn't be thrown.");
    }
  }
}
