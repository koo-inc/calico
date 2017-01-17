package calicosample.endpoint.userinfo;

import javax.inject.Inject;

import calicosample.service.UserInfoService;
import lombok.Getter;
import lombok.Setter;

public class IsUniqueLoginIdEndpoint extends UserInfoEndpoint<IsUniqueLoginIdEndpoint.Input, Boolean> {
  @Inject private UserInfoService userInfoService;

  @Getter @Setter
  public static class Input {
    private String loginId;
    private Integer exceptId;
  }

  @Override
  public Boolean execute(Input input) {
    return userInfoService.isUniqueLoginId(input.getLoginId(), input.getExceptId());
  }
}
