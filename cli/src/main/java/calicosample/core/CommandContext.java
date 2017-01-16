package calicosample.core;

import java.time.LocalDateTime;

import calicosample.core.auth.CalicoSampleAuthInfo;
import jp.co.freemind.calico.auth.AuthToken;
import jp.co.freemind.calico.context.Context;
import lombok.Getter;

/**
 * Created by kakusuke on 15/07/16.
 */
public class CommandContext implements Context<CalicoSampleAuthInfo> {
  @Getter private LocalDateTime processDateTime = LocalDateTime.now();
  @Getter private String remoteAddress = "localhost";
  @Getter private AuthToken authToken = AuthToken.createEmpty(getRemoteAddress(), processDateTime);
  private CalicoSampleAuthInfo authInfo = CalicoSampleAuthInfo.ofNull();

  @Override
  public CalicoSampleAuthInfo getAuthInfo() {
    return authInfo;
  }
}
