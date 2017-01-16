package calicosample.endpoint.customer;

import javax.inject.Inject;

import calicosample.dao.CustomerDao;
import calicosample.entity.Customer;

public class UpdateFormEndpoint extends CustomerEndpoint<CustomerEndpoint.IdInput, UpdateEndpoint.Input> {
  @Inject private CustomerDao customerDao;

  @Override
  public UpdateEndpoint.Input execute(IdInput input) {
    Customer customer = customerDao.findById(input.getId());
    return UpdateEndpoint.Input.of(customer, customerDao.findFamiliesByCustomer(customer));
  }
}
