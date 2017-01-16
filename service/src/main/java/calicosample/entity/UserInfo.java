package calicosample.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserInfo extends CalicoSampleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String loginId;
  private String password;

  public static final UserInfo BATCH_USER = new UserInfo();
  static {
    BATCH_USER.setId(0);
    BATCH_USER.setLoginId("(batch)");
    BATCH_USER.setPassword("");
  }
}
