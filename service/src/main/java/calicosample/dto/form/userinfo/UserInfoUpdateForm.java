package calicosample.dto.form.userinfo;

import calicosample.entity.UserInfo;
import jp.co.freemind.calico.dto.DTOUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserInfoUpdateForm extends UserInfoGeneralForm {

  @NotNull
  private Integer id;

  /**
   * create
   */
  public static UserInfoUpdateForm create(UserInfo userInfo){
    return DTOUtil.copyProperties(new UserInfoUpdateForm(), userInfo);
  }

  /**
   * loginId ユニークチェック
   */
  @AssertTrue(message = "既に使用されているログインIDです。")
  private boolean isUniqueLoginId(){
    if(StringUtils.isEmpty(getLoginId())){
      return true;
    }
    return getUserInfoDao().findForLoginIdUniqueCheck(getLoginId(), getId()).size() == 0;
  }

}
