package calicosample.entity;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import jp.co.freemind.calico.core.auth.Authority;
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

  @Entity
  @Getter @Setter
  public static class WithAdditionalData extends UserInfo {
    private AuthorityList authorities;

    public Set<Authority> getAuthorities() {
      if (authorities == null) return Collections.emptySet();
      return authorities.stream()
        .map(r -> (Authority) r)
        .collect(toSet());
    }

    public boolean isUsedBySystem() {
      return this == BATCH_USER;
    }
  }

}
