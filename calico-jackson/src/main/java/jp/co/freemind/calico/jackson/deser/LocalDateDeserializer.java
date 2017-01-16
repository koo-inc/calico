package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
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
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
  private final DateTimeFormatter formatter;
  public LocalDateDeserializer(DateTimeFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
      String string = p.getText().trim();
      if (string.length() == 0) {
        return null;
      }
      TemporalAccessor accessor = formatter.parse(string);
      LocalDate date = accessor.query(TemporalQueries.localDate());
      if (date != null) {
        return date;
      }
      Instant instant = Instant.from(accessor);
      return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    throw ctxt.wrongTokenException(p, JsonToken.VALUE_STRING, "Expected string.");
  }
}
