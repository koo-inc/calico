package jp.co.freemind.calico.core.zone;

@FunctionalInterface
public interface Executable<T> {
  T execute() throws Throwable;
}
