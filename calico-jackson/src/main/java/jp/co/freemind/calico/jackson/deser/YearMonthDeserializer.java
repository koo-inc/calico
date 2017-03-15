package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class YearMonthDeserializer extends JsonDeserializer<YearMonth> {
  private final DateTimeFormatter formatter;
  public YearMonthDeserializer(DateTimeFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public YearMonth deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
      String string = p.getText().trim();
      if (string.length() == 0) {
        return null;
      }
      TemporalAccessor accessor = formatter.parse(string);
      LocalDate date = accessor.query(TemporalQueries.localDate());
      if (date != null) {
        return YearMonth.from(date);
      }
      Instant instant = Instant.from(accessor);
      return YearMonth.from(instant.atZone(ZoneId.systemDefault()).toLocalDate());
    }
    throw ctxt.wrongTokenException(p, JsonToken.VALUE_STRING, "Expected string.");
  }
}

