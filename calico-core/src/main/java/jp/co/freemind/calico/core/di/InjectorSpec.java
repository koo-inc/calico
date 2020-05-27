package jp.co.freemind.calico.core.di;

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

public class InjectorSpec {
  private final Class<? extends Annotation> scope;
  private final Consumable<Throwable> onError;
  private final Runnable onFinish;
  private final Module[] modules;
  private final Map<Key<?>, Provider<?>> providers;

  InjectorSpec() {
    this(null, null, null, new Module[0], Collections.emptyMap());
  }
  private InjectorSpec(@Nullable Class<? extends Annotation> scope, @Nullable Consumable<Throwable> onError, @Nullable Runnable onFinish, @Nonnull Module[] modules, @Nonnull Map<Key<?>, Provider<?>> providers) {
    this.scope = scope;
    this.onError = onError;
    this.onFinish = onFinish;
    this.modules = modules;
    this.providers = providers;
  }

  public InjectorSpec scope(@Nonnull Class<? extends Annotation> scope) {
    return new InjectorSpec(scope, onError, onFinish, modules, providers);
  }
  public InjectorSpec onError(@Nonnull Consumable<Throwable> onError) {
    return new InjectorSpec(scope, onError, onFinish, modules, providers);
  }
  public InjectorSpec onFinish(@Nonnull Runnable onFinish) {
    return new InjectorSpec(scope, onError, onFinish, modules, providers);
  }
  public InjectorSpec modules(@Nonnull Module... modules) {
    return new InjectorSpec(scope, onError, onFinish, modules, providers);
  }
  public <T> InjectorSpec provide(Key<T> key, Provider<? extends T> provider) {
    Map<Key<?>, Provider<?>> newProviders = ImmutableMap.<Key<?>, Provider<?>>builder().putAll(providers).put(key, provider).build();
    return new InjectorSpec(scope, onError, onFinish, modules, newProviders);
  }
  public <T> InjectorSpec provide(Class<T> type, Provider<? extends T> provider) {
    return provide(Key.get(type), provider);
  }
  public <T, K extends T> InjectorSpec provide(Key<T> key, K instance) {
    Provider<K> provider = ()-> instance;
    return provide(key, provider);
  }
  public <T, K extends T> InjectorSpec provide(Class<T> type, K instance) {
    return provide(Key.get(type), instance);
  }
  @SuppressWarnings("unchecked")
  public <T> InjectorSpec provide(T instance) {
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
