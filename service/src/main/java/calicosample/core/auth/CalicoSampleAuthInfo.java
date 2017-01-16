package calicosample.core.auth;

import calicosample.entity.UserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public class CalicoSampleAuthInfo implements jp.co.freemind.calico.auth.AuthInfo {

  private Integer userId;
  private String loginId;

  /**
   * create
   */
  public static CalicoSampleAuthInfo of(@NonNull UserInfo userInfo){
    return new CalicoSampleAuthInfo(userInfo.getId(), userInfo.getLoginId());
  }

  public static CalicoSampleAuthInfo ofNull() {
    return new CalicoSampleAuthInfo(null, null);
  }

  @Override
  public boolean isAuthenticated() {
    return userId != null;
  }
}
