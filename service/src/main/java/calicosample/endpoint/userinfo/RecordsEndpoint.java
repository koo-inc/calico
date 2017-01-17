package calicosample.endpoint.userinfo;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.endpoint.dto.EmptyInput;
import lombok.Getter;
import lombok.Setter;

public class RecordsEndpoint extends UserInfoEndpoint<EmptyInput, List<RecordsEndpoint.Output>> {
  @Inject private UserInfoDao userInfoDao;

  @Getter @Setter
  public static class Output {
    private Integer id;
    private String loginId;
  }

  @Override
  public List<RecordsEndpoint.Output> execute(EmptyInput form) {
    return userInfoDao.findAll().stream()
      .map(userInfo -> DTOUtil.copyProperties(new Output(), userInfo))
      .collect(Collectors.toList());
  }
}
