package jp.co.freemind.calico.jackson.ser;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Created by tasuku on 15/03/23.
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
  private final DateTimeFormatter formatter;

  public LocalTimeSerializer(DateTimeFormatter formatter) {
    this.formatter = formatter.withZone(ZoneId.systemDefault());
  }

  @Override
  public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (formatter.getZone() != null) {
      Instant instant = value.atDate(LocalDate.ofEpochDay(0)).atZone(ZoneId.systemDefault()).toInstant();
      gen.writeString(formatter.format(instant));
    }
    else {
      gen.writeString(value.format(formatter));
    }
  }
}
