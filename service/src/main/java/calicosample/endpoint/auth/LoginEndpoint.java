package calicosample.endpoint.auth;

import com.google.inject.Inject;

import calicosample.service.AuthService;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.di.Context;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

public class LoginEndpoint implements Endpoint<LoginEndpoint.Input, AuthInfo> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Getter @Setter
  public static class Input {
    private String loginId;
    private String password;
  }

  @Override
  public AuthInfo execute(Input input) {
    AuthInfo authInfo = authService.login(input.getLoginId(), input.getPassword());
    context.setAuthInfo(authInfo);
    return authInfo;
  }
}
