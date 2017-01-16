package jp.co.freemind.calico.guice;

import com.google.inject.Injector;

public abstract class InjectorFactory {
  private Injector injector = createInjector();

  public Injector getInjector() {
    return injector;
  }

  protected abstract Injector createInjector();
}
