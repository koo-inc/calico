package calicosample.core.doma;

import java.util.function.Supplier;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seasar.doma.jdbc.AbstractJdbcLogger;

/**
 * Created by kakusuke on 15/06/22.
 */
public class Log4j2JdbcLogger extends AbstractJdbcLogger<Level> {
  /**
   * ログレベルを指定してインスタンスを構築します。
   *
   * @param level ログレベル
   */
  public Log4j2JdbcLogger(Level level) {
    super(level);
  }
  public Log4j2JdbcLogger() {
    super(Level.INFO);
  }

  @Override
  protected void log(Level level, String callerClassName, String callerMethodName, Throwable throwable, Supplier<String> messageSupplier) {
    Logger logger = LogManager.getLogger(callerClassName);
    logger.log(level, messageSupplier.get(), throwable);
  }
}
