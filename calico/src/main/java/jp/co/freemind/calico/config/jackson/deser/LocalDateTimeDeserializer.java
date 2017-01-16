package jp.co.freemind.calico.config.jackson.deser;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Created by tasuku on 15/03/23.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
  private final DateTimeFormatter formatter;
  public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
      String string = p.getText().trim();
      if (string.length() == 0) {
        return null;
      }
      TemporalAccessor accessor = formatter.parse(string);
      LocalDate date = accessor.query(TemporalQueries.localDate());
      LocalTime time = accessor.query(TemporalQueries.localTime());
      if (date != null && time != null) {
        return LocalDateTime.of(date, time);
      }
      Instant instant = Instant.from(accessor);
      return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    throw ctxt.wrongTokenException(p, JsonToken.VALUE_STRING, "Expected string.");
  }
}
