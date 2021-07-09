package calicosample.core.auth;

import static java.util.Collections.emptySet;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;

import calicosample.entity.UserInfo;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.auth.Authority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public class CalicoSampleAuthInfo implements AuthInfo {

  private Integer userId;
  private String loginId;
  private Set<Authority> authorities;
  @JsonIgnore
  private AuthToken authToken;
  private boolean isSystemUsed;

  /**
   * create
   */
  public static CalicoSampleAuthInfo of(@NonNull UserInfo.WithAdditionalData userInfo, String remoteAddress, LocalDateTime processDateTime){
    AuthToken authToken = AuthToken.create(userInfo.getLoginId(), remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(userInfo.getId(), userInfo.getLoginId(), userInfo.getAuthorities(), authToken, userInfo.isUsedBySystem());
  }

  public static CalicoSampleAuthInfo of(@NonNull AuthToken authToken, Function<AuthToken, Optional<UserInfo.WithAdditionalData>> authenticator){
    return authenticator.apply(authToken)
      .map(ui -> new CalicoSampleAuthInfo(ui.getId(), ui.getLoginId(), ui.getAuthorities(), authToken, ui.isUsedBySystem()))
      .orElseGet(() -> new CalicoSampleAuthInfo(null, null, emptySet(), authToken, false));
  }

  public static CalicoSampleAuthInfo ofNull(String remoteAddress, LocalDateTime processDateTime) {
    AuthToken authToken = AuthToken.createEmpty(remoteAddress, processDateTime);
    return new CalicoSampleAuthInfo(null, null, emptySet(), authToken, false);
  }

  @Override
  public boolean isAuthenticated() {
    return userId != null;
  }

  public Set<Authority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public AuthToken getAuthToken() {
    return authToken;
  }
}
