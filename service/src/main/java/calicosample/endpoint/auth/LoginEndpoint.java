package calicosample.endpoint.auth;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.service.AuthService;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.di.Context;
import lombok.Getter;
import lombok.Setter;

public class LoginEndpoint implements Endpoint<LoginEndpoint.Input, Result> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Getter @Setter
  public static class Input {
    private String loginId;
    private String password;
  }

  @Override
  public Result execute(Input input) {
    CalicoSampleAuthInfo authInfo = authService.login(input.getLoginId(), input.getPassword());
    return new Result(context.extend(s -> s.authInfo(authInfo)), authInfo);
  }
}
