package calicosample.dto.record.userinfo;

import calicosample.entity.UserInfo;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.dto.Record;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserInfoRecord extends Record {

  private Integer id;
  private String loginId;

  public static UserInfoRecord create(UserInfo userInfo){
    return DTOUtil.copyProperties(new UserInfoRecord(), userInfo);
  }

}
