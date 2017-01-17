package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.dto.DTOUtil;

public class UpdateFormEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, UpdateEndpoint.Input> {
  @Inject private UserInfoDao userInfoDao;

  @Override
  public UpdateEndpoint.Input execute(IdInput input) {
    return DTOUtil.copyProperties(new UpdateEndpoint.Input(), userInfoDao.findById(input.getId()));
  }
}
