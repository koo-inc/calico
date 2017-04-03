package jp.co.freemind.calico.jackson.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jp.co.freemind.calico.core.orm.Identifier;

public class IdentifierSerializer extends JsonSerializer<Identifier<?>> {
  @Override
  public void serialize(Identifier<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.getCodec().writeValue(gen, value.getValue());
  }
}
