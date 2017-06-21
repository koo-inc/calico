package jp.co.freemind.calico.core.zone;

import java.util.Optional;
import java.util.concurrent.Callable;

class ZoneCallable<V> implements Callable<Optional<V>> {
  private final Zone zone;
  private final Executable<V> executable;

  ZoneCallable(Zone zone, Executable<V> executable) {
    this.zone = zone;
    this.executable = executable;
  }

  @Override
  public Optional<V> call() throws Exception {
    return zone.call(executable);
  }
}
