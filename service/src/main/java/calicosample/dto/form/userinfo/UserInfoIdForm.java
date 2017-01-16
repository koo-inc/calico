package calicosample.dto.form.userinfo;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserInfoIdForm extends Form {

  @NotNull
  private Integer id;

  /**
   * create
   */
  public static UserInfoIdForm create(Integer id){
    UserInfoIdForm form = new UserInfoIdForm();
    form.setId(id);
    return form;
  }
}
