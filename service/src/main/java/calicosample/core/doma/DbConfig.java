package calicosample.core.doma;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

/**
 * Created by tasuku on 15/03/09.
 */

@Singleton
public class DbConfig implements Config {
  @Getter
  private final TransactionManager transactionManager;
  @Getter
  private final DataSource dataSource;
  @Getter
  private final Dialect dialect;
  @Getter
  private final JdbcLogger jdbcLogger = new Log4j2JdbcLogger();

  @Inject
  public DbConfig(LocalTransactionDataSource dataSource, Dialect dialect) {
    this.dialect = dialect;
    this.dataSource = dataSource;
    this.transactionManager = new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
  }

  @Override
  public Naming getNaming() {
    return Naming.SNAKE_LOWER_CASE;
  }
}
