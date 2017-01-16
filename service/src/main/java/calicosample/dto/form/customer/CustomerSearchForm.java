package calicosample.dto.form.customer;

import java.util.Optional;

import calicosample.core.dto.SearchForm;
import calicosample.extenum.Sex;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerSearchForm extends SearchForm {
  private Optional<String> name;
  private Sex sex;
  {
    _page = new Page();
    _sort = new Sort("name", SortType.ASC);
  }
}
