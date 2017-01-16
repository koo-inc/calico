package jp.co.freemind.calico.config.jackson.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jp.co.freemind.calico.extenum.ExtEnum;

/**
 * Created by kakusuke on 15/07/30.
 */
public class ExtEnumNameSerializer extends JsonSerializer<ExtEnum<?>> {
  @Override
  public void serialize(ExtEnum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeString(value.getName());
  }
}
