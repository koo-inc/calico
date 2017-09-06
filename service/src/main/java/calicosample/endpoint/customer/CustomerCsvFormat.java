package calicosample.endpoint.customer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.Nullable;

import calicosample.extenum.Sex;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.freemind.calico.csv.CsvColumn;
import jp.co.freemind.calico.jackson.deser.ExtEnumNameDeserializer;
import jp.co.freemind.calico.jackson.deser.LocalDateDeserializer;
import jp.co.freemind.calico.jackson.ser.ExtEnumNameSerializer;
import jp.co.freemind.calico.jackson.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

@Getter @Setter
public class CustomerCsvFormat {
  @CsvColumn(order = 1, name = "姓")
  private String kname1;

  @CsvColumn(order = 2, name = "名")
  private String kname2;

  @Nullable
  @CsvColumn(order = 3, name = "姓(カナ)")
  private String fname1;

  @Nullable
  @CsvColumn(order = 4, name = "名(カナ)")
  private String fname2;

  @Nullable
  @JsonSerialize(using = ExtEnumNameSerializer.class)
  @JsonDeserialize(using = ExtEnumNameDeserializer.class)
  @CsvColumn(order = 5, name = "性別")
  private Sex sex;

  @Nullable
  @CsvColumn(order = 6, name = "好きな数字")
  private Integer favoriteNumber;

  @Nullable
  @JsonSerialize(using = CsvDateSerializer.class)
  @JsonDeserialize(using = CsvDateDeserializer.class)
  @CsvColumn(order = 7, name = "生年月日")
  private LocalDate birthday;

  @CsvColumn(order = 8, name = "クレーマー")
  @JsonSerialize(using = BooleanSerializer.class)
  @JsonDeserialize(using = BooleanDeserializer.class)
  private boolean claimer;

  public static class CsvDateSerializer extends LocalDateSerializer {
    public CsvDateSerializer() {
      super(DateTimeFormatter.ofPattern("uuuu/MM/dd"));
    }
  }
  public static class CsvDateDeserializer extends LocalDateDeserializer {
    public CsvDateDeserializer() {
      super(DateTimeFormatter.ofPattern("uuuu/M/d"));
    }
  }
  public static class BooleanSerializer extends JsonSerializer<Boolean> {
    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
      gen.writeString(value == null || !value ? "×" : "○");
    }
  }
  public static class BooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      return Strings.isNotEmpty(p.getValueAsString());
    }
  }
}
