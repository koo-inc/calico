package calicosample.endpoint.auth;

import javax.inject.Inject;

import calicosample.service.AuthService;
import jp.co.freemind.calico.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import jp.co.freemind.calico.endpoint.Endpoint;

public class LogoutEndpoint extends Endpoint<EmptyInput, EmptyOutput> {
  @Inject private AuthService authService;

  @Override
  public EmptyOutput execute(EmptyInput input) {
    authService.logout();
    return EmptyOutput.getInstance();
  }
}
