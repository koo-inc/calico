package jp.co.freemind.calico.core.di;

@FunctionalInterface
public interface Processable {
  void proceed() throws Throwable;

  default Executable<?> toExecutable() {
    return () -> {
      this.proceed();
      return null;
    };
  }
}
