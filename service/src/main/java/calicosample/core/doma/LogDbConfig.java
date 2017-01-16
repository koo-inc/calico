package calicosample.core.doma;

import javax.sql.DataSource;

import org.apache.logging.log4j.Level;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.Getter;

/**
 * Created by tasuku on 15/03/09.
 */

@Singleton
public class LogDbConfig implements Config {
  @Getter
  private final DataSource dataSource;
  @Getter
  private final Dialect dialect;
  @Getter
  private final JdbcLogger jdbcLogger = new Log4j2JdbcLogger(Level.TRACE);

  @Inject
  public LogDbConfig(@Named("log") DataSource dataSource, Dialect dialect) {
    this.dialect = dialect;
    this.dataSource = dataSource;
  }

  @Override
  public Naming getNaming() {
    return Naming.SNAKE_LOWER_CASE;
  }
}
