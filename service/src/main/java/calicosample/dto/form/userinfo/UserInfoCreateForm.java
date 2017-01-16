package calicosample.dto.form.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Getter @Setter
public class UserInfoCreateForm extends UserInfoGeneralForm {

  /**
   * loginId ユニークチェック
   */
  @AssertTrue(message = "既に使用されているログインIDです。")
  private boolean isUniqueLoginId(){
    if(StringUtils.isEmpty(getLoginId())){
      return true;
    }
    return getUserInfoDao().findForLoginIdUniqueCheck(getLoginId()).size() == 0;
  }

}
