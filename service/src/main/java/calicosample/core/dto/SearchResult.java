package calicosample.core.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class SearchResult<T> {
  private Long _count;
  private List<T> records;
}
