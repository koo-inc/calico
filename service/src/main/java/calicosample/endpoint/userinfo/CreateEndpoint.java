package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.Messages;
import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import com.google.common.base.Strings;
import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.validation.Violation;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.Getter;
import lombok.Setter;

public class CreateEndpoint extends UserInfoEndpoint<CreateEndpoint.Input, UserInfoEndpoint.IdOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Input extends UserInfoEndpoint.CommonFormInput {
    @Validate
    private void validateUniqueLoginId(Violation violation){
      if(Strings.isNullOrEmpty(getLoginId())){
        return;
      }
      if (Zone.getCurrent().getInstance(UserInfoDao.class).findForLoginIdUniqueCheck(getLoginId()).size() > 0) {
        violation.mark("id", Messages.UNIQUE_LOGIN_ID.value());
      }
    }
  }

  @Override
  public IdOutput execute(Input input) {
    UserInfo userInfo = new UserInfo();
    input.copyTo(userInfo);
    userInfoDao.isnert(userInfo);
    return IdOutput.of(userInfo.getId());
  }
}
