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
public class AccessEndLog extends LogEntity implements EndLog {
  public AccessEndLog() {}

  private long startLogId;
  private String resultCode;
  private long interval;

  @Transient @NonNull
  private AccessStartLog startLog;
  public StartLog getStartLog() { return startLog; }
}
