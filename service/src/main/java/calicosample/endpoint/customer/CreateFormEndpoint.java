package calicosample.endpoint.customer;

import jp.co.freemind.calico.endpoint.dto.EmptyInput;

public class CreateFormEndpoint extends CustomerEndpoint<EmptyInput, CreateEndpoint.Input> {

  @Override
  public CreateEndpoint.Input execute(EmptyInput input) {
    return new CreateEndpoint.Input(){{
      setClaimer(false);
    }};
  }
}
