package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.Messages;
import calicosample.core.auth.AllowOnly;
import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import calicosample.extenum.CalicoSampleAuthority;
import com.google.common.base.Strings;
import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.validation.Violation;
import jp.co.freemind.calico.core.di.InjectorRef;
import lombok.Getter;
import lombok.Setter;

@AllowOnly(CalicoSampleAuthority.USER_INFO_WRITE)
public class CreateEndpoint extends UserInfoEndpoint<CreateEndpoint.Input, UserInfoEndpoint.IdOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Input extends UserInfoEndpoint.CommonFormInput {
    @Validate
    private void validateUniqueLoginId(Violation violation){
      if(Strings.isNullOrEmpty(getLoginId())){
        return;
      }
      if (InjectorRef.getCurrent().getInstance(UserInfoDao.class).findForLoginIdUniqueCheck(getLoginId()).size() > 0) {
        violation.mark("id", Messages.UNIQUE_LOGIN_ID.value());
      }
    }
  }

  @Override
  public IdOutput execute(Input input) {
    UserInfo userInfo = new UserInfo();
    input.copyTo(userInfo);
    userInfoDao.insert(userInfo);
    return IdOutput.of(userInfo.getId());
  }
}
