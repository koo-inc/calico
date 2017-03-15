package calicosample.extenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@Domain(valueType = Integer.class, factoryMethod = "of")
public enum FamilyType implements ExtEnum<Integer> {
  PARENT(1, "親"),
  PARTNER(2, "夫・妻"),
  SIBLING(3, "兄弟・姉妹"),
  CHILD(4, "子供"),
  OTHER(5, "その他");

  private final Integer id;
  private final String name;
  FamilyType(Integer id, String name) { this.id = id; this.name = name; }
  @JsonCreator public static FamilyType of(Object id){ return ExtEnum.of(id); }
  public Integer getValue() { return id; }
}
