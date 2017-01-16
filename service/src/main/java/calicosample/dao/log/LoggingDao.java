package calicosample.dao.log;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.Insert;
import org.seasar.doma.Procedure;
import calicosample.core.doma.InjectLogConfig;
import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.BatchEndLog;
import calicosample.entity.log.BatchStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.entity.log.JsLog;

/**
 * Created by kakusuke on 15/06/14.
 */
@Dao
@InjectLogConfig
public interface LoggingDao {
  @Insert
  int insert(AccessStartLog log);
  @Insert
  int insert(AccessEndLog log);
  @Insert
  int insert(BatchStartLog log);
  @Insert
  int insert(BatchEndLog log);
  @Insert
  int insert(ErrorLog log);
  @Insert
  int insert(JsLog log);
  @Procedure
  void rotate(@In String tableName);
}
