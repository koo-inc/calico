package calicosample.endpoint.customer;

import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;

public class SearchFormEndpoint extends CustomerEndpoint<EmptyInput, SearchEndpoint.Input> {

  @Override
  public SearchEndpoint.Input execute(EmptyInput input) {
    return new SearchEndpoint.Input();
  }
}
