package calicosample.endpoint.customer;

import javax.inject.Inject;

import calicosample.entity.Customer;
import calicosample.service.CustomerService;
import lombok.Getter;
import lombok.Setter;

public class CreateEndpoint extends CustomerEndpoint<CreateEndpoint.Input, CustomerEndpoint.IdOutput> {
  @Inject private CustomerService customerService;

  @Getter @Setter
  public static class Input extends CustomerEndpoint.CommonFormInput {
  }

  @Override
  public CustomerEndpoint.IdOutput execute(Input input) {
    Customer customer = customerService.create(input);
    return new CustomerEndpoint.IdOutput(){{
      setId(customer.getId());
    }};
  }
}
