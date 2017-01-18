package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;

public class UpdateFormEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, UpdateEndpoint.Input> {
  @Inject private UserInfoDao userInfoDao;

  @Override
  public UpdateEndpoint.Input execute(IdInput input) {
    return UpdateEndpoint.Input.of(userInfoDao.findById(input.getId()));
  }
}
