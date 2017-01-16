package jp.co.freemind.calico.config.jackson.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jp.co.freemind.calico.media.Media;

/**
 * Created by tasuku on 15/03/23.
 */
public class MediaSerializer extends JsonSerializer<Media> {
  @Override
  public void serialize(Media value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    serialize(value, gen, value.getId() == null);
  }

  public static class WithPayload extends JsonSerializer<Media> {
    @Override
    public void serialize(Media value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
      MediaSerializer.serialize(value, gen, true);
    }
  }

  private static void serialize(Media value, JsonGenerator gen, boolean withPayload) throws IOException {
    gen.writeStartObject();
    if (value.getId() != null) {
      gen.writeObjectField("id", value.getId());
    }
    if (withPayload) {
      gen.writeObjectField("payload", value.getPayload());
    }
    gen.writeObjectField("meta", value.getMeta());
    gen.writeEndObject();
  }
}
