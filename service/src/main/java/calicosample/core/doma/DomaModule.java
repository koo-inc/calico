package calicosample.core.doma;

import java.util.stream.Stream;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import jp.co.freemind.calico.core.util.ClassFinder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

public class DomaModule extends AbstractModule {
  @Override
  public void configure() {
    requireBinding(Dialect.class);
    requireBinding(LocalTransactionDataSource.class);
    bind(Config.class).to(DbConfig.class);
    bind(Config.class).annotatedWith(Names.named("log")).to(LogDbConfig.class);

    ClassFinder.findClasses("calicosample.dao").stream()
      .filter(c -> !c.isInterface())
      .forEach(c ->
          Stream.of(c.getInterfaces()).forEach(i ->
              bindAnyway(i).to(c).in(Scopes.SINGLETON)
          )
      );
  }

  @SuppressWarnings("unchecked")
  private AnnotatedBindingBuilder<Object> bindAnyway(Class<?> iface) {
    return bind((Class<Object>) iface);
  }
}
