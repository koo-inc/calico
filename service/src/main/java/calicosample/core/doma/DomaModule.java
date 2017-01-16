package calicosample.core.doma;

import java.util.stream.Stream;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.util.ClassFinder;

/**
 * Created by tasuku on 15/03/09.
 */
public class DomaModule extends AbstractModule {
  @Override
  public void configure() {
    requireBinding(Dialect.class);
    requireBinding(LocalTransactionDataSource.class);
    bind(Config.class).to(DbConfig.class);
    bind(Config.class).annotatedWith(Names.named("log")).to(LogDbConfig.class);

    ClassFinder.findClasses(Env.getRootPackage() + ".dao").stream()
      .filter(c -> !c.isInterface())
      .forEach(c ->
          Stream.of(c.getInterfaces()).forEach(i ->
              bind(stupidCast(i)).to(uglyStupidCast(c)).in(Scopes.SINGLETON)
          )
      );
  }

  @SuppressWarnings("unchecked")
  private <T> Class<T> stupidCast(Class<?> clazz) {
    return (Class<T>) clazz;
  }
  @SuppressWarnings("unchecked")
  private <T, U extends T> Class<U> uglyStupidCast(Class<?> clazz) {
    return (Class<U>) clazz;
  }
}
