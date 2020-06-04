package calicosample.core;

import static calicosample.core.validator.Validations.instanceOf;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.joining;
import static jp.co.freemind.calico.core.endpoint.validation.FieldAccessor.annotatedWith;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import calicosample.Messages;
import calicosample.core.doma.DomaModule;
import calicosample.core.doma.PgDialect;
import calicosample.core.fmstorage.FmStorage;
import calicosample.core.mail.PostManProvider;
import calicosample.core.validator.AllowEmpty;
import calicosample.core.validator.AllowedExtensions;
import calicosample.core.validator.FileSize;
import calicosample.core.validator.LetterCount;
import calicosample.core.validator.LowerBound;
import calicosample.core.validator.UpperBound;
import calicosample.core.validator.Validations;
import jp.co.freemind.calico.core.config.Registry;
import jp.co.freemind.calico.core.config.Setting;
import jp.co.freemind.calico.core.di.SimpleScope;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.validation.FieldAccessor;
import jp.co.freemind.calico.core.endpoint.validation.Validator;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaStorage;
import jp.co.freemind.calico.core.util.ClassFinder;
import jp.co.freemind.calico.jackson.ObjectMapperProvider;
import jp.co.freemind.calico.mail.PostMan;
import lombok.SneakyThrows;

public class ServiceModule extends AbstractModule {
  private SimpleScope transactionScope;
  @Override
  protected void configure() {
    install(new DomaModule());

    ClassFinder.findClasses("jp.co.freemind.calico", "calicosample").stream()
      .filter(c -> c.isAnnotationPresent(Setting.class))
      .forEach(this::bindSetting);

    bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);
    bind(MediaStorage.class).to(FmStorage.class);
    bind(PostMan.class).toProvider(PostManProvider.class);

    transactionScope = new SimpleScope();
    bindScope(TransactionScoped.class, transactionScope);
  }

  @SuppressWarnings("unchecked")
  private void bindSetting(Class<?> clazz) {
    bind((Class<Object>) clazz).toProvider(new Provider<Object>() {
      @Inject
      private Registry registry;

      @Override
      public Object get() {
        return registry.loadSetting(clazz);
      }
    }).in(Singleton.class);
  }

  @Provides @Singleton @Inject
  protected LocalTransactionDataSource provideLocalTransactionDataSource(MainDbSetting dbSetting) {
    return new LocalTransactionDataSource(getDataSource(dbSetting));
  }
  @Provides @Singleton @Named("log") @Inject
  protected DataSource provideLogDbDataSource(LogDbSetting dbSetting) {
    if (dbSetting.getJdbcUrl() == null) return new SimpleDataSource();
    return getDataSource(dbSetting);
  }

  @Provides @Named("transactionScope")
  protected SimpleScope provideTransactionScope() {
    return transactionScope;
  }

  @SneakyThrows
  private DataSource getDataSource(DbSettingBase dbEnv) {
    SimpleDataSource dataSource = new SimpleDataSource();
    Optional.ofNullable(dbEnv.getJdbcUrl()).ifPresent(dataSource::setUrl);
    dataSource.setUser(dbEnv.getUser());
    dataSource.setPassword(dbEnv.getPassword());
    return dataSource;
  }

  @Provides @Singleton
  protected Dialect provideDialect() {
    return new PgDialect();
  }

  @Provides @Singleton
  protected Validator provideValidator() {
    return Validator.factory()
      .when(annotatedWith(Nullable.class).negate())
      .then(Validations::notNull, Messages.REQUIRED)

      .when(instanceOf(Collections.class).and(annotatedWith(AllowEmpty.class).negate()))
      .then(Validations::notEmpty, Messages.NOT_EMPTY)

      .when(instanceOf(Map.class).and(annotatedWith(AllowEmpty.class).negate()))
      .then(Validations::notEmpty, Messages.NOT_EMPTY)

      .when(instanceOf(Number.class).and(annotatedWith(LowerBound.class)))
      .then(Validations::lowerBound, Messages.LOWER_BOUND(f ->
        singletonMap("value", annotation(f, LowerBound.class, LowerBound::value))))

      .when(instanceOf(Number.class).and(annotatedWith(UpperBound.class)))
      .then(Validations::upperBound, Messages.UPPER_BOUND(f ->
        singletonMap("value", annotation(f, LowerBound.class, LowerBound::value))))

      .when(instanceOf(Media.class).and(annotatedWith(FileSize.class)))
      .then(Validations::fileSize, Messages.FILE_SIZE(f ->
        ImmutableMap.<String, Object>builder()
          .put("upperBound", annotation(f, FileSize.class, FileSize::upperBound))
          .put("lowerBound", annotation(f, FileSize.class, FileSize::lowerBound))
          .build()
      ))

      .when(instanceOf(String.class).and(annotatedWith(LetterCount.class)))
      .then(Validations::letterCount, Messages.LETTER_COUNT(f ->
        ImmutableMap.<String, Object>builder()
          .put("upperBound", annotation(f, LetterCount.class, LetterCount::upperBound))
          .put("lowerBound", annotation(f, LetterCount.class, LetterCount::lowerBound))
          .build()
      ))

      .when(instanceOf(Media.class).and(annotatedWith(AllowedExtensions.class)))
      .then(Validations::allowedExtensions, Messages.ALLOWED_EXTENSIONS(f ->
        singletonMap("value", Arrays.stream(annotation(f, AllowedExtensions.class, AllowedExtensions::value))
          .collect(joining(", ")))))

      .getValidator();
  }

  private <T, A extends Annotation> T annotation(FieldAccessor field, Class<A> type, Function<A, T> converter) {
    return converter.apply(field.getAnnotation(type));
  }
}
