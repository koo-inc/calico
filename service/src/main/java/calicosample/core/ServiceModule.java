package calicosample.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.doma.DomaModule;
import calicosample.core.doma.PgDialect;
import calicosample.core.fmstorage.FmStorage;
import calicosample.core.mail.FmMailer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.config.env.EnvPath;
import jp.co.freemind.calico.config.env.PartialEnv;
import jp.co.freemind.calico.config.jackson.ObjectMapperProvider;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.EndpointManager;
import jp.co.freemind.calico.endpoint.interceptor.EndpointTransactionInterceptor;
import jp.co.freemind.calico.endpoint.interceptor.EndpointValidationInterceptor;
import jp.co.freemind.calico.mail.Mailer;
import jp.co.freemind.calico.media.MediaStorage;
import jp.co.freemind.calico.util.ClassFinder;
import lombok.SneakyThrows;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

/**
 * Created by tasuku on 15/04/08.
 */
public class ServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new DomaModule());

    ClassFinder.findClasses(Env.getRootPackage() + ".service").stream()
      .filter(c -> !Modifier.isAbstract(c.getModifiers()))
      .forEach(c -> bind(c));

    ClassFinder.findClasses(Env.getRootPackage() + ".endpoint").stream()
      .filter(c -> !Modifier.isAbstract(c.getModifiers()))
      .forEach(c -> bind(c));

    bindInterceptor(
      Matchers.inSubpackage(Env.getRootPackage() + ".endpoint").and(Matchers.subclassesOf(Endpoint.class)),
      new AbstractMatcher<Method>() {
        @Override
        public boolean matches(Method method) {
          return method.getReturnType() != Object.class && method.getName().equals("execute");
        }
      },
      new EndpointTransactionInterceptor(),
      new EndpointValidationInterceptor()
    );

    bind(ObjectMapper.class).toInstance(ObjectMapperProvider.createMapper());
    bind(Validator.class).toInstance(buildValidator());
    bind(MediaStorage.class).to(FmStorage.class);
    bind(Mailer.class).to(FmMailer.class);
    bind(EndpointManager.class).toInstance(new EndpointManager());

    requireBinding(Key.get(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {}));
  }


  @Provides @Singleton
  protected LocalTransactionDataSource provideLocalTransactionDataSource() {
    DbEnv dbEnv = Env.loadPartial(MainDbEnv.class);
    return new LocalTransactionDataSource(getDataSource(dbEnv));
  }
  @Provides @Singleton @Named("log")
  protected DataSource provideLogDbDataSource() {
    DbEnv dbEnv = Env.loadPartial(LogDbEnv.class);
    if (dbEnv.getJdbcUrl() == null) return new SimpleDataSource();
    return getDataSource(dbEnv);
  }

  @SneakyThrows
  private DataSource getDataSource(DbEnv dbEnv) {
    SimpleDataSource dataSource = new SimpleDataSource();
    Optional.ofNullable(dbEnv.getJdbcUrl()).ifPresent(dataSource::setUrl);
    dataSource.setUser(dbEnv.getUser());
    dataSource.setPassword(dbEnv.getPassword());
    return dataSource;
  }

  private Validator buildValidator() {
    return Validation.byProvider(HibernateValidator.class).configure()
      .ignoreXmlConfiguration()
      .messageInterpolator(new ResourceBundleMessageInterpolator(this::createMessageResourceBundleWithUtf8))
      .buildValidatorFactory().getValidator();
  }

  @SneakyThrows
  private ResourceBundle createMessageResourceBundleWithUtf8(Locale locale) {
    try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ValidationMessages.properties");
         InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)) {
      return new PropertyResourceBundle(isr);
    }
  }

  @Provides @Singleton
  protected Dialect provideDialect() {
    return new PgDialect();
  }

  public interface DbEnv extends PartialEnv {
    String getJdbcUrl();
    String getUser();
    String getPassword();
  }

  @EnvPath("ds.main") public interface MainDbEnv extends DbEnv { }
  @EnvPath("ds.log") public interface LogDbEnv extends DbEnv { }

  static class PublicMethodMatcher extends AbstractMatcher<Method> {

    @Override
    public boolean matches(Method method) {
      return Modifier.isPublic(method.getModifiers());
    }
  }
}
