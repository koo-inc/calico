package calicosample.dto.record.customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import calicosample.domain.AdditionalInfoList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.config.jackson.deser.ExtEnumNameDeserializer;
import jp.co.freemind.calico.config.jackson.ser.ExtEnumNameSerializer;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.dto.Record;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerRecord extends Record {

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
   * CustomerFamily
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

    @JsonPropertyOrder({"顧客ID", "家族ID", "家族形態", "名前", "性別", "好きな数字", "誕生日"})
    public abstract class CsvFormat {
      @JsonProperty("家族ID")
      public Integer id;
      @JsonProperty("顧客ID")
      public Integer customerId;
      @JsonProperty("家族形態")
      @JsonDeserialize(using = ExtEnumNameDeserializer.class)
      @JsonSerialize(using = ExtEnumNameSerializer.class)
      public FamilyType familyType;
      @JsonProperty("名前")
      public String name;
      @JsonProperty("性別")
      @JsonDeserialize(using = ExtEnumNameDeserializer.class)
      @JsonSerialize(using = ExtEnumNameSerializer.class)
      public Sex sex;
      @JsonProperty("好きな数字")
      public Integer favoriteNumber;
      @JsonProperty("誕生日")
      public LocalDate birthday;
    }
  }

  /**
   * create
   */
  public static CustomerRecord create(Customer customer, List<CustomerFamily> customerFamilies){
    CustomerRecord record = DTOUtil.copyProperties(new CustomerRecord(), customer);
    for(CustomerFamily customerFamily : customerFamilies){
      record.families.add(DTOUtil.copyProperties(new CustomerFamilyRecord(), customerFamily));
    }
    return record;
  }

}
