package jp.co.freemind.calico.core.zone;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Modules;

public class ZoneSpec {
  private final Class<? extends Annotation> scope;
  private final Consumable<Throwable> onError;
  private final Runnable onFinish;
  private final Module[] modules;
  private final Map<Key<?>, Provider<?>> providers;

  ZoneSpec() {
    this(null, null, null, new Module[0], Collections.emptyMap());
  }
  private ZoneSpec(@Nullable Class<? extends Annotation> scope, @Nullable Consumable<Throwable> onError, @Nullable Runnable onFinish, @Nonnull Module[] modules, @Nonnull Map<Key<?>, Provider<?>> providers) {
    this.scope = scope;
    this.onError = onError;
    this.onFinish = onFinish;
    this.modules = modules;
    this.providers = providers;
  }

  public ZoneSpec scope(@Nonnull Class<? extends Annotation> scope) {
    return new ZoneSpec(scope, onError, onFinish, modules, providers);
  }
  public ZoneSpec onError(@Nonnull Consumable<Throwable> onError) {
    return new ZoneSpec(scope, onError, onFinish, modules, providers);
  }
  public ZoneSpec onFinish(@Nonnull Runnable onFinish) {
    return new ZoneSpec(scope, onError, onFinish, modules, providers);
  }
  public ZoneSpec modules(@Nonnull Module... modules) {
    return new ZoneSpec(scope, onError, onFinish, modules, providers);
  }
  public <T> ZoneSpec provide(Key<T> key, Provider<? extends T> provider) {
    Map<Key<?>, Provider<?>> newProviders = ImmutableMap.<Key<?>, Provider<?>>builder().putAll(providers).put(key, provider).build();
    return new ZoneSpec(scope, onError, onFinish, modules, newProviders);
  }
  public <T> ZoneSpec provide(Class<T> type, Provider<? extends T> provider) {
    return provide(Key.get(type), provider);
  }
  public <T, K extends T> ZoneSpec provide(Key<T> key, K instance) {
    Provider<K> provider = ()-> instance;
    return provide(key, provider);
  }
  public <T, K extends T> ZoneSpec provide(Class<T> type, K instance) {
    return provide(Key.get(type), instance);
  }
  @SuppressWarnings("unchecked")
  public <T> ZoneSpec provide(T instance) {
    return provide((Class<T>) instance.getClass(), instance);
  }


  Optional<Module> getModule() {
    if (modules.length == 0) return Optional.empty();
    return Optional.of(Arrays.stream(modules)
      .reduce(Modules.EMPTY_MODULE, (m1, m2)-> Modules.override(m1).with(m2)));
  }

  Optional<Class<? extends Annotation>> getScope() {
    return Optional.ofNullable(scope);
  }

  Map<Key<?>, Provider<?>> getProviders() {
    return providers;
  }

  boolean doOnError(Throwable t) throws Throwable {
    if(onError != null) {
      onError.consume(t);
      return true;
    }
    return false;
  }

  void doFinish() {
    if (this.onFinish != null) {
      this.onFinish.run();
    }
  }
}
