package calicosample.endpoint.userinfo;

import javax.inject.Inject;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

public class UpdateEndpoint extends UserInfoEndpoint<UpdateEndpoint.Input, EmptyOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Input extends UserInfoEndpoint.CommonFormInput {
    @NotNull
    private Integer id;

    @AssertTrue(message = "既に使用されているログインIDです。")
    private boolean isUniqueLoginId(){
      if(StringUtils.isEmpty(getLoginId())){
        return true;
      }
      return InjectUtil.getInstance(UserInfoDao.class).findForLoginIdUniqueCheck(getLoginId(), getId()).size() == 0;
    }

    public static Input of(UserInfo userInfo){
      return new Input(){{
        copyFrom(userInfo);
        setId(userInfo.getId());
      }};
    }
  }

  @Override
  public EmptyOutput execute(Input input) {
    UserInfo userInfo = userInfoDao.findById(input.getId());
    input.copyTo(userInfo);
    userInfoDao.update(userInfo);
    return EmptyOutput.getInstance();
  }
}
