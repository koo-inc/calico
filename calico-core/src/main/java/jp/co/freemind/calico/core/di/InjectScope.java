package jp.co.freemind.calico.core.di;

import java.lang.annotation.Annotation;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class InjectScope implements Scope {
  private final Class<? extends Annotation> scope;

  public InjectScope(Class<? extends Annotation> scope) {
    this.scope = scope;
  }
  @Override
  public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
    return ()-> InjectorRef.getCurrent().getProvider(scope, key, unscoped).get();
  }
}
