package calicosample.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import calicosample.core.fmstorage.MediaIdGenerator;
import calicosample.dao.CustomerDao;
import calicosample.endpoint.customer.CreateEndpoint;
import calicosample.endpoint.customer.CustomerEndpoint;
import calicosample.endpoint.customer.UpdateEndpoint;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaStorage;
import jp.co.freemind.calico.service.Service;

import static jp.co.freemind.calico.dto.DTOUtil.copyProperties;

public class CustomerService extends Service {
  @Inject private CustomerDao customerDao;
  @Inject private MediaStorage mediaStorage;

  public Customer create(CreateEndpoint.Input input) {
    Customer customer = copyProperties(new Customer(), input);
    input.getPhoto().ifPresent(photo -> {
      Media proxy = mediaStorage.store(photo, MediaIdGenerator::customerPhoto);
      customer.setPhoto(Optional.of(proxy));
    });
    customerDao.insert(customer);

    updateCustomerFamilies(customer, input.getFamilies());

    return customer;
  }

  public Customer update(UpdateEndpoint.Input input) {
    Customer customer = customerDao.findById(input.getId());
    copyProperties(customer, input);

    Optional<Media> oldPhoto = customer.getOriginalStates().getPhoto();
    input.getPhoto().ifPresent(photo -> {
      if(photo.getId() == null){
        Media proxy = mediaStorage.store(photo, MediaIdGenerator::customerPhoto);
        customer.setPhoto(Optional.of(proxy));
      }
    });

    customerDao.update(customer);
    updateCustomerFamilies(customer, input.getFamilies());

    oldPhoto.ifPresent(op -> {
      if (!customer.getPhoto().isPresent()) {
        mediaStorage.remove(op);
      } else {
        if (!op.getId().equals(customer.getPhoto().get().getId())) {
          mediaStorage.remove(op);
        }
      }
    });

    return customer;
  }

  private void updateCustomerFamilies(Customer customer, List<CustomerEndpoint.CommonFormInput.Family> familyForms) {
    familyForms.stream().forEach(f -> {
      CustomerFamily family = copyProperties(new CustomerFamily(), f);
      if (f.getDelete()) {
        if (family.getId() != null) {
          customerDao.deleteFamily(family);
        }
      } else if (f.getId() != null) {
        customerDao.updateFamily(family);
      } else if (f.getId() == null) {
        customerDao.insertFamily(customer, family);
      }
    });
  }

  public Customer delete(Integer id) {
    Customer customer = customerDao.findById(id);
    List<CustomerFamily> families = customerDao.findFamiliesByCustomer(customer);
    families.forEach(f -> customerDao.deleteFamily(f));
    customerDao.delete(customer);
    customer.getPhoto().ifPresent(mediaStorage::remove);
    return customer;
  }
}
