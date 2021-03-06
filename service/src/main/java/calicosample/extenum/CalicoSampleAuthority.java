package calicosample.extenum;

import jp.co.freemind.calico.core.auth.Authority;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
public enum CalicoSampleAuthority implements Authority {
  USER_INFO_READ("ユーザー閲覧権限"),
  USER_INFO_WRITE("ユーザー編集権限"),
  ;

  private final String name;
  CalicoSampleAuthority(String name) {
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

  public static CalicoSampleAuthority of(String id) {
    return ExtEnum.of(id, CalicoSampleAuthority.class);
  }
}
