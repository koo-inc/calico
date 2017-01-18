package calicosample.endpoint.customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import calicosample.dao.CustomerDao;
import calicosample.domain.AdditionalInfoList;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;

public class RecordEndpoint extends CustomerEndpoint<CustomerEndpoint.IdInput, RecordEndpoint.Output> {
  @Inject private CustomerDao customerDao;

  @Getter @Setter
  public static class Output {

    private Integer id;
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
    private Optional<String> email;
    private String homepageUrl;
    private String phoneNumber;
    private Optional<Media> photo;
    private AdditionalInfoList additionalInfoList;

    /**
     * Family
     */
    private List<CustomerFamilyRecord> families = new ArrayList<>();

    @Getter @Setter
    public static class CustomerFamilyRecord {
      private Integer id;
      private Integer customerId;
      private FamilyType familyType;
      private String name;
      private Sex sex;
      private Integer favoriteNumber;
      private LocalDate birthday;
    }

    public static Output create(Customer customer, List<CustomerFamily> customerFamilies){
      Output record = DTOUtil.copyProperties(new Output(), customer);
      for(CustomerFamily customerFamily : customerFamilies){
        record.families.add(DTOUtil.copyProperties(new CustomerFamilyRecord(), customerFamily));
      }
      return record;
    }
  }

  @Override
  public Output execute(IdInput input) {
    Customer customer = customerDao.findById(input.getId());
    return Output.create(customer, customerDao.findFamiliesByCustomer(customer));
  }
}
