package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jp.co.freemind.calico.core.extenum.ExtEnum;

public class ExtEnumDeserializer extends JsonDeserializer<ExtEnum<?>> {

  private final Class<? extends ExtEnum<?>> enumClass;

  public ExtEnumDeserializer(Class<? extends ExtEnum<?>> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public ExtEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    switch(p.getCurrentToken()) {
      case START_OBJECT:
        return getExtEnum(getIdValue(p));
      case FIELD_NAME:
        return getExtEnum(p.getCurrentName());
      case VALUE_NULL:
        return null;
      case VALUE_FALSE:
      case VALUE_TRUE:
      case VALUE_NUMBER_FLOAT:
      case VALUE_NUMBER_INT:
      case VALUE_STRING:
        return getExtEnum(p.getValueAsString());
    }

    return null;
  }

  private String getIdValue(JsonParser p) throws IOException {
    while(p.getCurrentToken() != null) {
      JsonToken token = p.nextToken();
      switch(token) {
        case FIELD_NAME:
          if (p.getCurrentName().equals("id")) {
            switch (p.nextToken()) {
              case VALUE_NULL: return eatAndGetValue(p, null);
              case VALUE_TRUE: return eatAndGetValue(p, true);
              case VALUE_FALSE: return eatAndGetValue(p, false);
              case VALUE_NUMBER_FLOAT: return eatAndGetValue(p, p.getFloatValue());
              case VALUE_NUMBER_INT: return eatAndGetValue(p, p.getBigIntegerValue());
              case VALUE_STRING: return eatAndGetValue(p, p.getText());
              default: return null;
            }
          }
          break;
        case END_OBJECT:
          return null;
      }
    }
    return null;
  }

  private String eatAndGetValue(JsonParser p, Object value) throws IOException {
    while(p.nextToken() != JsonToken.END_OBJECT) { }
    return value != null ? String.valueOf(value) : null;
  }

  private ExtEnum<?> getExtEnum(@Nullable String id) {
    try {
      Method values = enumClass.getMethod("values");
      for (ExtEnum<?> value : (ExtEnum<?>[]) values.invoke(null)) {
        if (String.valueOf(value.getId()).equals(String.valueOf(id))) {
          return value;
        }
      }
    }
    catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
    return null;
  }
}
