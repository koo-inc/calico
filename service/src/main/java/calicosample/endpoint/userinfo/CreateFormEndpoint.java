package calicosample.endpoint.userinfo;

import jp.co.freemind.calico.endpoint.dto.EmptyInput;

public class CreateFormEndpoint extends UserInfoEndpoint<EmptyInput, CreateEndpoint.Input> {
  @Override
  public CreateEndpoint.Input execute(EmptyInput input) {
    return new CreateEndpoint.Input();
  }
}
