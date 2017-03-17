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

public class UpdateEndpoint extends UserInfoEndpoint<UpdateEndpoint.Input, UserInfoEndpoint.IdOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Input extends UserInfoEndpoint.CommonFormInput {
    private Integer id;

    @Validate
    private void isUniqueLoginId(Violation violation){
      if(Strings.isNullOrEmpty(getLoginId())) return;
      if(Zone.getCurrent().getInstance(UserInfoDao.class).findForLoginIdUniqueCheck(getLoginId(), getId()).size() > 0) {
        violation.mark("id", Messages.UNIQUE_LOGIN_ID.value());
      }
    }

    public static Input of(UserInfo userInfo){
      return new Input(){{
        copyFrom(userInfo);
        setId(userInfo.getId());
      }};
    }
  }

  @Override
  public IdOutput execute(Input input) {
    UserInfo userInfo = userInfoDao.findById(input.getId());
    input.copyTo(userInfo);
    userInfoDao.update(userInfo);
    return IdOutput.of(userInfo.getId());
  }
}
