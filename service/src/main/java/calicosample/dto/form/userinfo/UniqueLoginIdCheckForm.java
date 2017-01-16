package calicosample.dto.form.userinfo;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UniqueLoginIdCheckForm extends Form {

  private String loginId;

  private Integer exceptId;
}

