package calicosample.endpoint.customer;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.service.CustomerService;
import jp.co.freemind.calico.dto.DTOUtil;
import lombok.Getter;
import lombok.Setter;

public class UpdateEndpoint extends CustomerEndpoint<UpdateEndpoint.Input, CustomerEndpoint.IdOutput> {
  @Inject private CustomerService customerService;

  @Getter @Setter
  public static class Input extends CustomerEndpoint.CommonFormInput {
    @NotNull
    private Integer id;

    public static Input create(Customer customer, List<CustomerFamily> customerFamilies){
      Input input = DTOUtil.copyProperties(new Input(), customer);
      for(CustomerFamily customerFamily : customerFamilies){
        input.getFamilies().add(DTOUtil.copyProperties(new Family(), customerFamily));
      }
      return input;
    }
  }

  @Override
  public IdOutput execute(Input input) {
    Customer customer = customerService.update(input);
    return new IdOutput(){{
      setId(customer.getId());
    }};
  }
}
