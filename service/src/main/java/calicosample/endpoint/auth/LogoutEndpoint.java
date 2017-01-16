package calicosample.endpoint.auth;

import com.google.inject.Inject;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.service.AuthService;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.ContextualOutput;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.zone.Context;

public class LogoutEndpoint implements Endpoint<EmptyInput, ContextualOutput<CalicoSampleAuthInfo>> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Override
  public ContextualOutput<CalicoSampleAuthInfo> execute(EmptyInput input) {
    CalicoSampleAuthInfo authInfo = authService.logout();
    return new ContextualOutput<>(context.extend(s -> s.authInfo(authInfo)), authInfo);
  }
}
