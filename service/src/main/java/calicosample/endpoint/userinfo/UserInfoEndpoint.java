package calicosample.endpoint.userinfo;

import calicosample.core.validator.LetterCount;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

public abstract class UserInfoEndpoint<INPUT, OUTPUT> implements Endpoint<INPUT, OUTPUT> {

  @Getter @Setter
  public static class IdInput {
    private Integer id;
  }

  @Getter @Setter
  public static class IdOutput {
    private Integer id;

    public static IdOutput of(Integer id){
      IdOutput output = new IdOutput();
      output.setId(id);
      return output;
    }
  }

  @Getter @Setter
  public static abstract class CommonFormInput {
    @LetterCount(lowerBound = 3, upperBound = 10)
    private String loginId;

    private String password;

    public void copyFrom(UserInfo userInfo){
      setLoginId(userInfo.getLoginId());
      setPassword(userInfo.getPassword());
    }

    public void copyTo(UserInfo userInfo){
      userInfo.setLoginId(getLoginId());
      userInfo.setPassword(getPassword());
    }
  }
}
