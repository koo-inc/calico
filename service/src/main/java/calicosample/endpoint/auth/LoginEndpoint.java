package calicosample.endpoint.auth;

import javax.inject.Inject;

import calicosample.service.AuthService;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import jp.co.freemind.calico.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginEndpoint extends Endpoint<LoginEndpoint.Input, EmptyOutput> {
  @Inject private AuthService authService;

  @Getter @Setter
  public static class Input {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
  }

  @Override
  public EmptyOutput execute(Input input) {
    authService.login(input.getLoginId(), input.getPassword());
    return EmptyOutput.getInstance();
  }
}
