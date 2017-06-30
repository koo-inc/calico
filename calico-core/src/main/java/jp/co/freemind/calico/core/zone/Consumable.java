package jp.co.freemind.calico.core.zone;

@FunctionalInterface
public interface Consumable<T> {
  void consume(T t) throws Throwable;
}
