package jp.co.freemind.calico.jackson.ser;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Created by tasuku on 15/03/23.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
  private final DateTimeFormatter formatter;

  public LocalDateTimeSerializer(DateTimeFormatter formatter) {
    this.formatter = formatter.withZone(ZoneId.systemDefault());
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    if (formatter.getZone() != null) {
      Instant instant = value.atZone(ZoneId.systemDefault()).toInstant();
      gen.writeString(formatter.format(instant));
    }
    else {
      gen.writeString(value.format(formatter));
    }
  }
}
