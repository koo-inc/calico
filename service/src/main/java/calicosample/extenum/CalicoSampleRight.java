package calicosample.extenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.freemind.calico.core.auth.Right;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
public enum CalicoSampleRight implements Right {
  USER_INFO_READ("ユーザー閲覧権限"),
  USER_INFO_WRITE("ユーザー編集権限"),
  ;

  private final String name;
  CalicoSampleRight(String name) {
    this.name = name;
  }

  @Override
  public String getId() {
    return this.name();
  }

  @Override
  public String getName() {
    return name;
  }

  public String getValue() {
    return getId();
  }

  @JsonCreator
  public static CalicoSampleRight of(String id) {
    return ExtEnum.of(id, CalicoSampleRight.class);
  }
}
