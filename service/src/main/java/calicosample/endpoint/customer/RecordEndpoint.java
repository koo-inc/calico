package calicosample.endpoint.customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import calicosample.dao.CustomerDao;
import calicosample.domain.AdditionalInfoList;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.core.media.Media;
import lombok.Getter;
import lombok.Setter;

public class RecordEndpoint extends CustomerEndpoint<CustomerEndpoint.IdInput, RecordEndpoint.Output> {
  @Inject private CustomerDao customerDao;

  @Getter @Setter
  public static class Output {

    private Customer.ID id;
    private String kname1;
    private String kname2;
    private String fname1;
    private String fname2;
    private Sex sex;
    private Integer favoriteNumber;
    private Boolean claimer;
    private LocalDate birthday;
    private LocalTime contactEnableStartTime;
    private LocalTime contactEnableEndTime;
    private String email;
    private String homepageUrl;
    private String phoneNumber;
    private Media photo;
    private AdditionalInfoList additionalInfoList;

    /**
     * Family
     */
    private List<CustomerFamilyRecord> families = new ArrayList<>();

    @Getter @Setter
    public static class CustomerFamilyRecord {
      private Integer id;
      private Customer.ID customerId;
      private FamilyType familyType;
      private String name;
      private Sex sex;
      private Integer favoriteNumber;
      private LocalDate birthday;
    }

    public static Output of(Customer customer, List<CustomerFamily> customerFamilies){
      Output output = new Output(){{
        setId(customer.getId());
        setKname1(customer.getKname1());
        setKname2(customer.getKname2());
        setFname1(customer.getFname1());
        setFname2(customer.getFname2());
        setSex(customer.getSex());
        setFavoriteNumber(customer.getFavoriteNumber());
        setClaimer(customer.getClaimer());
        setBirthday(customer.getBirthday());
        setContactEnableStartTime(customer.getContactEnableStartTime());
        setContactEnableEndTime(customer.getContactEnableEndTime());
        setEmail(customer.getEmail());
        setHomepageUrl(customer.getHomepageUrl());
        setPhoneNumber(customer.getPhoneNumber());
        setPhoto(customer.getPhoto());
        setAdditionalInfoList(customer.getAdditionalInfoList());
      }};
      for(CustomerFamily customerFamily : customerFamilies){
        output.families.add(new CustomerFamilyRecord(){{
          setId(customerFamily.getId());
          setCustomerId(customerFamily.getCustomerId());
          setFamilyType(customerFamily.getFamilyType());
          setName(customerFamily.getName());
          setSex(customerFamily.getSex());
          setFavoriteNumber(customerFamily.getFavoriteNumber());
          setBirthday(customerFamily.getBirthday());
        }});
      }
      return output;
    }
  }

  @Override
  public Output execute(IdInput input) {
    Customer customer = customerDao.findById(input.getId());
    return Output.of(customer, customerDao.findFamiliesByCustomer(customer));
  }
}
