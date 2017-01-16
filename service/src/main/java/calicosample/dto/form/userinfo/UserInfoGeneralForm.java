package calicosample.dto.form.userinfo;

import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.dto.Form;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.inject.Inject;
import javax.validation.constraints.Size;

@Getter @Setter
public class UserInfoGeneralForm extends Form {

  @NotEmpty
  @Size(min = 3, max = 10)
  private String loginId;

  @NotEmpty
  private String password;

  /**
   * Service
   */
  @Getter(AccessLevel.PROTECTED)
  @Inject private UserInfoDao userInfoDao;
}
