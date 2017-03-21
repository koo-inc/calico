package calicosample.core.auth;

import static java.util.Collections.emptySet;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import calicosample.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.auth.Rights;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public class CalicoSampleAuthInfo implements AuthInfo {

  private Integer userId;
  private String loginId;
  private Set<Rights> rights;
  @JsonIgnore
  private AuthToken authToken;

  /**
   * create
   */
  public static CalicoSampleAuthInfo of(@NonNull UserInfo.WithAdditionalData userInfo, String remoteAddress, LocalDateTime processDateTime){
    AuthToken authToken = AuthToken.create(userInfo.getLoginId(), remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(userInfo.getId(), userInfo.getLoginId(), userInfo.getRightsSet(), authToken);
  }

  public static CalicoSampleAuthInfo of(@NonNull AuthToken authToken, Function<AuthToken, Optional<UserInfo.WithAdditionalData>> authenticator){
    return authenticator.apply(authToken)
      .map(ui -> new CalicoSampleAuthInfo(ui.getId(), ui.getLoginId(), ui.getRightsSet(), authToken))
      .orElseGet(() -> new CalicoSampleAuthInfo(null, null, emptySet(), authToken));
  }

  public static CalicoSampleAuthInfo ofNull(String remoteAddress, LocalDateTime processDateTime) {
    AuthToken authToken = AuthToken.createEmpty(remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(null, null, emptySet(), authToken);
  }

  @Override
  public boolean isAuthenticated() {
    return userId != null;
  }

  @Override
  public boolean hasRights(Rights rights) {
    return this.rights.contains(rights);
  }

  @Override
  public AuthToken getAuthToken() {
    return authToken;
  }
}
