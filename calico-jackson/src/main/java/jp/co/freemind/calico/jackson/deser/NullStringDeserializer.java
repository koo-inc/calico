package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class NullStringDeserializer extends JsonDeserializer<String> {
  private StringDeserializer deserializer = new StringDeserializer();
  @Override
  public String getNullValue(DeserializationContext ctxt) throws JsonMappingException {
    return null;
  }
  @Override
  public String getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
    return null;
  }

  @Override public boolean isCachable() {
    return deserializer.isCachable();
  }
  @Override
  public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    return normalize(deserializer.deserialize(jp, ctxt));
  }
  @Override
  public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
    return normalize(deserializer.deserializeWithType(jp, ctxt, typeDeserializer));
  }
  @Override
  public Class<?> handledType() {
    return deserializer.handledType();
  }
  public JavaType getValueType() {
    return deserializer.getValueType();
  }
  @Override
  public String deserialize(JsonParser p, DeserializationContext ctxt, String intoValue) throws IOException, JsonProcessingException {
    return normalize(deserializer.deserialize(p, ctxt, intoValue));
  }
  @Override
  public JsonDeserializer<String> unwrappingDeserializer(NameTransformer unwrapper) {
    return deserializer.unwrappingDeserializer(unwrapper);
  }
  @Override
  public JsonDeserializer<?> replaceDelegatee(JsonDeserializer<?> delegatee) {
    return deserializer.replaceDelegatee(delegatee);
  }
  @Override
  public JsonDeserializer<?> getDelegatee() {
    return deserializer.getDelegatee();
  }
  @Override
  public Collection<Object> getKnownPropertyNames() {
    return deserializer.getKnownPropertyNames();
  }

  @Override
  public ObjectIdReader getObjectIdReader() {
    return deserializer.getObjectIdReader();
  }
  @Override
  public SettableBeanProperty findBackReference(String refName) {
    return deserializer.findBackReference(refName);
  }

  private String normalize(String value) {
    if (value == null) return null;
    value = value.trim();
    return value.equals("") ? null : value;
  }
}
