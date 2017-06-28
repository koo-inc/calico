package calicosample.entity.log;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by kakusuke on 15/06/14.
 */
public interface EndLog extends Log {
  StartLog getStartLog();

  void setStartLogId(long id);
  void setInterval(long until);

  default void preInsert() {
    StartLog startLog = getStartLog();
    setStartLogId(startLog.getId());
    setInterval(ChronoUnit.MILLIS.between(startLog.getTs(), LocalDateTime.now()));
    setKey(startLog.getKey());
  }
}
