package calicosample.endpoint.system.batch;

import java.util.List;
import java.util.stream.Collectors;

import calicosample.core.batch.Batch;
import calicosample.core.batch.BatchFinder;
import calicosample.core.batch.BatchIndex;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyInput;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RecordsEndpoint extends Endpoint<EmptyInput, List<RecordsEndpoint.Record>> {

  @Getter @Setter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Record {
    private final String index;
    private final String label;
    private final String description;

    public static Record of(Class<? extends Batch> batch) {
      BatchIndex index = batch.getAnnotation(BatchIndex.class);
      return new Record(index.index(), index.label(), index.description());
    }
  }

  @Override
  public List<Record> execute(EmptyInput input) {
    return BatchFinder.findAll().stream().map(Record::of).collect(Collectors.toList());
  }
}
