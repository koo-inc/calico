package jp.co.freemind.calico.core.zone;

import java.lang.annotation.Annotation;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class ZoneScope implements Scope {
  private final Class<? extends Annotation> scope;

  public ZoneScope(Class<? extends Annotation> scope) {
    this.scope = scope;
  }
  @Override
  public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
    return ()-> Zone.getCurrent().getProvider(scope, key, unscoped).get();
  }
}
