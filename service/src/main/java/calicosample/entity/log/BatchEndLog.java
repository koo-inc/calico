package calicosample.entity.log;

import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by kakusuke on 15/06/14.
 */
@Entity
@Getter @Setter
@RequiredArgsConstructor(staticName = "of")
public class BatchEndLog extends LogEntity implements EndLog {
  public BatchEndLog() {}

  private long startLogId;
  private long interval;
  private String body;

  @Transient @NonNull
  private BatchStartLog startLog;
  public StartLog getStartLog() { return startLog; }
}
