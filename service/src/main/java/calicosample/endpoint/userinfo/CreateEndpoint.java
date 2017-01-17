package calicosample.endpoint.userinfo;

import javax.inject.Inject;
import javax.validation.constraints.AssertTrue;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

public class CreateEndpoint extends UserInfoEndpoint<CreateEndpoint.Input, EmptyOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Input extends UserInfoEndpoint.CommonFormInput {
    @AssertTrue(message = "既に使用されているログインIDです。")
    private boolean isUniqueLoginId(){
      if(StringUtils.isEmpty(getLoginId())){
        return true;
      }
      return InjectUtil.getInstance(UserInfoDao.class).findForLoginIdUniqueCheck(getLoginId()).size() == 0;
    }
  }

  @Override
  public EmptyOutput execute(Input input) {
    UserInfo userInfo = DTOUtil.copyProperties(new UserInfo(), input);
    userInfoDao.isnert(userInfo);
    return EmptyOutput.getInstance();
  }
}
