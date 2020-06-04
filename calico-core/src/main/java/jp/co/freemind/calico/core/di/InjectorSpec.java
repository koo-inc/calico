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
import com.google.inject.binder.ScopedBindingBuilder;

public class InjectorSpec {
  private final Class<? extends Annotation> scope;
  private final Module[] modules;
  private final Map<Key<?>, Provider<?>> providers;

  InjectorSpec() {
    this(null, new Module[0], Collections.emptyMap());
  }
  private InjectorSpec(@Nullable Class<? extends Annotation> scope, @Nonnull Module[] modules, @Nonnull Map<Key<?>, Provider<?>> providers) {
    this.scope = scope;
    this.modules = modules;
    this.providers = providers;
  }

  public InjectorSpec scope(@Nonnull Class<? extends Annotation> scope) {
    return new InjectorSpec(scope, modules, providers);
  }
  public InjectorSpec modules(@Nonnull Module... modules) {
    return new InjectorSpec(scope, modules, providers);
  }
  public <T> InjectorSpec provide(Key<T> key, Provider<? extends T> provider) {
    Map<Key<?>, Provider<?>> newProviders = ImmutableMap.<Key<?>, Provider<?>>builder().putAll(providers).put(key, provider).build();
    return new InjectorSpec(scope, modules, newProviders);
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


  @SuppressWarnings("rawtypes unchecked")
  Optional<Module> buildModule() {
    if (modules.length == 0 && providers.size() == 0) return Optional.empty();
    return Optional.of(binder -> {
      Arrays.stream(modules).forEach(binder::install);
      providers.forEach((key, provider) -> {
        ScopedBindingBuilder binding = binder.bind((Key) key).toProvider(provider);
        if (scope != null) {
          binding.in(scope);
        }
      });
    });
  }
}
