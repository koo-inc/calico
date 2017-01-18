package calicosample.endpoint.customer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import calicosample.core.dto.SearchForm;
import calicosample.core.dto.SearchResult;
import calicosample.dao.CustomerDao;
import calicosample.entity.Customer;
import calicosample.extenum.Sex;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.jdbc.SelectOptions;

public class SearchEndpoint extends CustomerEndpoint<SearchEndpoint.Input, SearchEndpoint.Output> {
  @Inject private CustomerDao customerDao;

  @Getter @Setter
  public static class Input extends SearchForm {
    private String name;
    private Sex sex;
    {
      _page = new Page();
      _sort = new Sort("name", SortType.ASC);
    }
  }

  @Getter @Setter
  public static class Output extends SearchResult<Record> {
    public static Output of(Long _count, List<Record> records){
      Output ret = new Output();
      ret.set_count(_count);
      ret.setRecords(records);
      return ret;
    }
  }

  @Getter @Setter
  public static class Record {
    private Integer id;
    private String kname1;
    private String kname2;
    private Sex sex;
    private LocalDate birthday;

    public static Record of(Customer customer){
      return new Record(){{
        setId(customer.getId());
        setKname1(customer.getKname1());
        setKname2(customer.getKname2());
        setSex(customer.getSex());
        setBirthday(customer.getBirthday());
      }};
    }
  }

  @Override
  public Output execute(Input input) {
    SelectOptions options = input.getSelectOptions();
    List<Record> records = customerDao.search(input, options, s ->
      s.map(Record::of).collect(Collectors.toList())
    );
    return Output.of(options.getCount(), records);
  }
}
