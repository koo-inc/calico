package calicosample.endpoint.customer;

import javax.inject.Inject;

import calicosample.service.CustomerService;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;

public class DeleteEndpoint extends CustomerEndpoint<CustomerEndpoint.IdInput, EmptyOutput> {
  @Inject private CustomerService customerService;

  @Override
  public EmptyOutput execute(IdInput input) {
    customerService.delete(input.getId());
    return EmptyOutput.getInstance();
  }
}
