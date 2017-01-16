package calicosample.core.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

import calicosample.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public class CalicoSampleAuthInfo implements AuthInfo {

  private Integer userId;
  private String loginId;
  @JsonIgnore
  private AuthToken authToken;

  /**
   * create
   */
  public static CalicoSampleAuthInfo of(@NonNull UserInfo userInfo, String remoteAddress, LocalDateTime processDateTime){
    AuthToken authToken = AuthToken.create(userInfo.getLoginId(), remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(userInfo.getId(), userInfo.getLoginId(), authToken);
  }

  public static CalicoSampleAuthInfo of(@NonNull AuthToken authToken, Function<AuthToken, Optional<UserInfo>> authenticator){
    return authenticator.apply(authToken)
      .map(ui -> new CalicoSampleAuthInfo(ui.getId(), ui.getLoginId(), authToken))
      .orElseGet(() -> new CalicoSampleAuthInfo(null, null, authToken));
  }

  public static CalicoSampleAuthInfo ofNull(String remoteAddress, LocalDateTime processDateTime) {
    AuthToken authToken = AuthToken.createEmpty(remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(null, null, authToken);
  }

  @Override
  public boolean isAuthenticated() {
    return userId != null;
  }

  @Override
  public AuthToken getAuthToken() {
    return authToken;
  }
}
