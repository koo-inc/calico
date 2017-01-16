package calicosample.core.dto;

import jp.co.freemind.calico.dto.Record;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public abstract class SearchResult<T extends Record> extends Record {
  private Long _count;
  private List<T> records;
}
