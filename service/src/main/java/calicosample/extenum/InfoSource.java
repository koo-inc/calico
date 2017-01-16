package calicosample.extenum;

import org.seasar.doma.Domain;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.freemind.calico.extenum.ExtEnum;
import lombok.Getter;

@Getter
@Domain(valueType = String.class, factoryMethod = "of")
public enum InfoSource implements ExtEnum<String> {
  PRIMARY("1st", "一次情報"),
  SECONDARY("2nd", "二次情報"),
  ;

  private final String id;
  private final String name;
  InfoSource(String id, String name) { this.id = id; this.name = name; }
  @JsonCreator public static InfoSource of(String id){ return ExtEnum.of(id); }
  public String getValue() { return id; }
}
