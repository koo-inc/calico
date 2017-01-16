package calicosample.entity.log;

import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kakusuke on 15/06/14.
 */
@Entity
@Getter @Setter
@ToString(callSuper = true)
public class ErrorLog extends LogEntity {
  public ErrorLog() {}

  private long startLogId;
  private String headers;
  private String exception;

  @Transient @NonNull StartLog startLog;

  @Override
  public void preInsert() {
    StartLog startLog = getStartLog();
    setStartLogId(startLog.getId());
    setKey(startLog.getKey());
  }

  public static ErrorLog of(StartLog startLog, Throwable e, String headers) {
    ErrorLog errorLog = new ErrorLog();
    errorLog.setStartLog(startLog);
    errorLog.setException(Throwables.getStackTraceAsString(e));
    errorLog.setHeaders(headers);
    return errorLog;
  }
}
