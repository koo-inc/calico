package calicosample.dto.record.system;

import calicosample.core.batch.Batch;
import calicosample.core.batch.BatchIndex;
import jp.co.freemind.calico.dto.Record;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakusuke on 15/07/16.
 */
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchIndexRecord extends Record {
  private final String index;
  private final String label;
  private final String description;

  public static BatchIndexRecord of(Class<? extends Batch> batch) {
    BatchIndex index = batch.getAnnotation(BatchIndex.class);
    return new BatchIndexRecord(index.index(), index.label(), index.description());
  }
}
