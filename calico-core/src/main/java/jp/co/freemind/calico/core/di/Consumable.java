package jp.co.freemind.calico.core.di;

@FunctionalInterface
public interface Consumable<T> {
  void consume(T t) throws Throwable;
}
