package jp.co.freemind.calico.core.zone;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Modules;

public class ZoneSpec {
  private final Class<? extends Annotation> scope;
  private final Consumer<Throwable> onError;
  private final Module[] modules;
  private final Map<Key<?>, Provider<?>> providers;

  ZoneSpec() {
    this(null, null, new Module[0], Collections.emptyMap());
  }
  private ZoneSpec(@Nullable Class<? extends Annotation> scope, @Nullable Consumer<Throwable> onError, @Nonnull Module[] modules, @Nonnull Map<Key<?>, Provider<?>> providers) {
    this.scope = scope;
    this.onError = onError;
    this.modules = modules;
    this.providers = providers;
  }

  public ZoneSpec scope(@Nonnull Class<? extends Annotation> scope) {
    return new ZoneSpec(scope, onError, modules, providers);
  }
  public ZoneSpec onError(@Nonnull Consumer<Throwable> onError) {
    return new ZoneSpec(scope, onError, modules, providers);
  }
  public ZoneSpec modules(@Nonnull Module... modules) {
    return new ZoneSpec(scope, onError, modules, providers);
  }
  public <T> ZoneSpec provide(Key<T> key, Provider<? extends T> provider) {
    Map<Key<?>, Provider<?>> newProviders = ImmutableMap.<Key<?>, Provider<?>>builder().putAll(providers).put(key, provider).build();
    return new ZoneSpec(scope, onError, modules, newProviders);
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

  boolean doOnError(Throwable t) {
    if(onError != null) {
      onError.accept(t);
      return true;
    }
    return false;
  }
}
