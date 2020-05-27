package jp.co.freemind.calico.core.di;

@FunctionalInterface
public interface Executable<T> {
  T execute() throws Throwable;
}
