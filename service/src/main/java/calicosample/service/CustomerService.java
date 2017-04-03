package calicosample.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import calicosample.core.fmstorage.MediaIdGenerator;
import calicosample.dao.CustomerDao;
import calicosample.endpoint.customer.CreateEndpoint;
import calicosample.endpoint.customer.CustomerEndpoint;
import calicosample.endpoint.customer.UpdateEndpoint;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaStorage;

public class CustomerService {
  @Inject private CustomerDao customerDao;
  @Inject private MediaStorage mediaStorage;

  public Customer create(CreateEndpoint.Input input) {
    Customer customer = new Customer();
    input.copyTo(customer);

    if(input.getPhoto() != null){
      Media proxy = mediaStorage.store(input.getPhoto(), MediaIdGenerator::customerPhoto);
      customer.setPhoto(proxy);
    }
    customerDao.insert(customer);

    updateCustomerFamilies(customer, input.getFamilies(), false);

    return customer;
  }

  public Customer update(UpdateEndpoint.Input input) {
    Customer customer = customerDao.findById(input.getId());
    input.copyTo(customer);

    Media oldPhoto = customer.getOriginalStates().getPhoto();
    if(input.getPhoto() != null){
      if(input.getPhoto().getId() == null){
        Media proxy = mediaStorage.store(input.getPhoto(), MediaIdGenerator::customerPhoto);
        customer.setPhoto(proxy);
      }
    }

    customerDao.update(customer);
    updateCustomerFamilies(customer, input.getFamilies(), true);

    if(oldPhoto != null){
      if (customer.getPhoto() == null) {
        mediaStorage.remove(oldPhoto);
      } else {
        if (!oldPhoto.getId().equals(customer.getPhoto().getId())) {
          mediaStorage.remove(oldPhoto);
        }
      }
    }

    return customer;
  }

  private void updateCustomerFamilies(Customer customer, List<CustomerEndpoint.CommonFormInput.Family> familyForms, boolean isUpdate) {
    if(isUpdate){
      List<Integer> ids = familyForms.stream()
        .map(CustomerEndpoint.CommonFormInput.Family::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
      customerDao.findFamiliesByCustomer(customer).stream()
        .filter(e -> !ids.contains(e.getId()))
        .forEach(e -> customerDao.deleteFamily(e));
    }
    familyForms.forEach(f -> {
      CustomerFamily family = new CustomerFamily();
      f.copyTo(family);
      if (f.getId() != null) {
        customerDao.updateFamily(family);
      } else if (f.getId() == null) {
        customerDao.insertFamily(customer, family);
      }
    });
  }

  public Customer delete(Customer.ID id) {
    Customer customer = customerDao.findById(id);
    List<CustomerFamily> families = customerDao.findFamiliesByCustomer(customer);
    families.forEach(f -> customerDao.deleteFamily(f));
    customerDao.delete(customer);
    if(customer.getPhoto() != null){
      mediaStorage.remove(customer.getPhoto());
    }
    return customer;
  }
}
