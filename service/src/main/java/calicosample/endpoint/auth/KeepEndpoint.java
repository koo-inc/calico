package calicosample.endpoint.auth;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.service.AuthService;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.zone.Context;

public class KeepEndpoint implements Endpoint<EmptyInput, Result> {
  @Inject private AuthService authService;
  @Inject private Context context;

  @Override
  public Result execute(EmptyInput input) {
    CalicoSampleAuthInfo authInfo = authService.keep(context.getAuthInfo());
    return new Result(context.extend(s -> s.authInfo(authInfo)), authInfo);
  }
}
