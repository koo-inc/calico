package calicosample.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.seasar.doma.jdbc.SelectOptions;

@Getter @Setter
public abstract class SearchForm {
  protected Page _page;
  protected Sort _sort;

  /**
   * SelectOptions
   */
  @JsonIgnore
  public SelectOptions getSelectOptions(){
    SelectOptions options = SelectOptions.get().count();
    if(_page != null){
      options.limit(_page.getPerPage());
      options.offset((_page.getNo() - 1) * _page.getPerPage());
    }
    return options;
  }

  /**
   * Page
   */
  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class Page {
    public static final Integer DEFAULT_PER_PAGE = 50;

    private Integer no = 1;
    private Integer perPage = DEFAULT_PER_PAGE;
  }

  /**
   * Sort
   */
  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class Sort {
    private String prop;
    private SortType type;

    @JsonIgnore
    public boolean isDesc(){
      return type == SortType.DESC;
    }
  }

  /**
   * SortType
   */
  @Getter
  public enum SortType {
    ASC,
    DESC;

    @JsonCreator
    public static SortType of(String name){
      if(Strings.isNullOrEmpty(name)) return null;
      return SortType.valueOf(name.toUpperCase());
    }

    @JsonValue
    public String getValue() {
      return name();
    }
  }

}
