package calicosample.endpoint.auth;

import com.google.inject.Inject;

import calicosample.service.AuthService;
import jp.co.freemind.calico.core.di.Context;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;

public class LogoutEndpoint implements Endpoint<EmptyInput, EmptyOutput> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Override
  public EmptyOutput execute(EmptyInput input) {
    context.setAuthInfo(authService.logout());
    return EmptyOutput.getInstance();
  }
}
