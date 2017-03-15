package calicosample.endpoint.userinfo;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import lombok.Getter;
import lombok.Setter;

public class RecordsEndpoint extends UserInfoEndpoint<EmptyInput, List<RecordsEndpoint.Output>> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Output {
    private Integer id;
    private String loginId;

    public static Output of(UserInfo entity){
      return new Output(){{
        setId(entity.getId());
        setLoginId(entity.getLoginId());
      }};
    }
  }

  @Override
  public List<RecordsEndpoint.Output> execute(EmptyInput form) {
    return userInfoDao.findAll().stream()
      .map(Output::of)
      .collect(Collectors.toList());
  }
}
