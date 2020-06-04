package calicosample.endpoint.auth;

import com.google.inject.Inject;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.service.AuthService;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.di.Context;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;

public class KeepEndpoint implements Endpoint<EmptyInput, AuthInfo> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Override
  public AuthInfo execute(EmptyInput input) {
    CalicoSampleAuthInfo authInfo = authService.keep(context.getAuthInfo());
    context.setAuthInfo(authInfo);
    return authInfo;
  }
}
