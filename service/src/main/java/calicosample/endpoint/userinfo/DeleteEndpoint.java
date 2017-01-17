package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;

public class DeleteEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, EmptyOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Override
  public EmptyOutput execute(IdInput input) {
    UserInfo userInfo = userInfoDao.findById(input.getId());
    userInfoDao.delete(userInfo);
    return EmptyOutput.getInstance();
  }
}
