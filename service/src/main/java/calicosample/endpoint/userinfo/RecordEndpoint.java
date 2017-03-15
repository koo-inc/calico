package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

public class RecordEndpoint extends UserInfoEndpoint<UserInfoEndpoint.IdInput, RecordEndpoint.Output> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Output {
    private Integer id;
    private String loginId;
    private String password;

    public static Output of(UserInfo entity){
      return new Output(){{
        setId(entity.getId());
        setLoginId(entity.getLoginId());
        setPassword(entity.getPassword());
      }};
    }
  }

  @Override
  public RecordEndpoint.Output execute(IdInput input) {
    return Output.of(userInfoDao.findById(input.getId()));
  }
}
