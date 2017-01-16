package calicosample.dto.record.customer;

import calicosample.core.dto.SearchResult;
import calicosample.entity.Customer;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.dto.DTOUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class CustomerSearchResult extends SearchResult<CustomerSearchResult.Record> {

  public static CustomerSearchResult of(Long _count, List<Record> records){
    CustomerSearchResult ret = new CustomerSearchResult();
    ret.set_count(_count);
    ret.setRecords(records);
    return ret;
  }

  @Getter @Setter
  public static class Record extends jp.co.freemind.calico.dto.Record {
    private Integer id;
    private String kname1;
    private String kname2;
    private Sex sex;
    private LocalDate birthday;

    public static Record of(Customer customer){
      return DTOUtil.copyProperties(new Record(), customer);
    }
  }
}
