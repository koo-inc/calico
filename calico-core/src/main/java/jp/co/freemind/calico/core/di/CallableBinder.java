package jp.co.freemind.calico.core.di;

import java.util.Optional;
import java.util.concurrent.Callable;

class CallableBinder<V> implements Callable<Optional<V>> {
  private final InjectorRef injectorRef;
  private final Executable<V> executable;

  CallableBinder(InjectorRef injectorRef, Executable<V> executable) {
    this.injectorRef = injectorRef;
    this.executable = executable;
  }

  @Override
  public Optional<V> call() throws Exception {
    return injectorRef.call(executable);
  }
}
