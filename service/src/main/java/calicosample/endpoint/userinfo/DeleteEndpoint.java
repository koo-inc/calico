package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.core.auth.AllowOnly;
import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import calicosample.extenum.CalicoSampleAuthority;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;

@AllowOnly(CalicoSampleAuthority.USER_INFO_WRITE)
public class DeleteEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, EmptyOutput> {
  @Inject private UserInfoDao userInfoDao;

  @Override
  public EmptyOutput execute(IdInput input) {
    UserInfo userInfo = userInfoDao.findById(input.getId());
    userInfoDao.delete(userInfo);
    return EmptyOutput.getInstance();
  }
}
