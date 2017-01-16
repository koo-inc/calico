package calicosample.dto.form.auth;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter @Setter
public class LoginForm extends Form {

  @NotEmpty
  private String loginId;

  @NotEmpty
  private String password;

}
