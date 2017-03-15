package jp.co.freemind.calico.jackson.ser;

import java.io.IOException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class YearMonthSerializer extends JsonSerializer<YearMonth> {
  private final DateTimeFormatter formatter;

  public YearMonthSerializer(DateTimeFormatter formatter) {
    this.formatter = formatter.withZone(ZoneId.systemDefault());
  }

  @Override
  public void serialize(YearMonth value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    if (formatter.getZone() != null) {
      Instant instant = value.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
      gen.writeString(formatter.format(instant));
    }
    else {
      gen.writeString(value.format(formatter));
    }
  }

}
