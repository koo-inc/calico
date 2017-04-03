package calicosample.endpoint.customer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import javax.inject.Inject;

import calicosample.dao.CustomerDao;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import calicosample.util.CsvUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jp.co.freemind.calico.jackson.deser.ExtEnumNameDeserializer;
import jp.co.freemind.calico.jackson.ser.ExtEnumNameSerializer;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.jackson.media.WithPayload;
import jp.co.freemind.csv.CsvMapper;
import jp.co.freemind.csv.CsvWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class DownloadFamilyCsvEndpoint extends CustomerEndpoint<CustomerEndpoint.IdInput, DownloadFamilyCsvEndpoint.Output> {
  @Inject private CustomerDao customerDao;
  private final CsvMapper<CustomerFamilyRecord> csvMapper = CsvUtil.createMapperForDownload(CustomerFamilyRecord.class, CustomerFamilyRecord.CsvFormat.class);

  @Getter @Setter
  public static class Output {
    @WithPayload
    private Media csv;

    public Output(Media csv) {
      this.csv = csv;
    }
  }

  @Getter @Setter
  public static class CustomerFamilyRecord {
    private Integer id;
    private Customer.ID customerId;
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

    public static CustomerFamilyRecord of(CustomerFamily entity){
      return new CustomerFamilyRecord(){{
        setId(entity.getId());
        setCustomerId(entity.getCustomerId());
        setFamilyType(entity.getFamilyType());
        setName(entity.getName());
        setSex(entity.getSex());
        setFavoriteNumber(entity.getFavoriteNumber());
        setBirthday(entity.getBirthday());
      }};
    }
  }

  @Override
  @SneakyThrows
  public Output execute(IdInput input) {
    Customer customer = customerDao.findById(input.getId());

    try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      CsvWriter<CustomerFamilyRecord> writer = csvMapper.createWriter();
      customerDao.findFamiliesByCustomer(customer, stream-> {
        stream.map(CustomerFamilyRecord::of).forEach(writer.writeTo(os));
        return null;
      });

      return new Output(Media.create(os.toByteArray(), "顧客家族情報.csv", "text/csv"));
    }
  }
}
