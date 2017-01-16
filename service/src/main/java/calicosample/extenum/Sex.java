package calicosample.extenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import lombok.Getter;
import org.seasar.doma.Domain;


@Getter
@Domain(valueType = Integer.class, factoryMethod = "of")
public enum Sex implements ExtEnum<Integer> {
  MAN(1, "男", "男性"),
  WOMAN(2, "女", "女性");

  private final Integer id;
  private final String name;
  private final String longName;
  Sex(Integer id, String name, String longName) { this.id = id; this.name = name; this.longName = longName;}
  @JsonCreator public static Sex of(Object id){ return ExtEnum.of(id); }
  public Integer getValue() { return id; }
}
