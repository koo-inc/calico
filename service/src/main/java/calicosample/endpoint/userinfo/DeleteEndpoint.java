package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.core.auth.Restriction;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;

@Restriction(WriteAuthZRule.class)
public class DeleteEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, EmptyOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Override
  public EmptyOutput execute(IdInput input) {
    UserInfo userInfo = userInfoDao.findById(input.getId());
    userInfoDao.delete(userInfo);
    return EmptyOutput.getInstance();
  }
}
