package jp.co.freemind.calico.core.di;

public class UnhandledException extends RuntimeException {
  private UnhandledException(Throwable e) {
    super(e.getMessage(), e);
  }

  public static UnhandledException of(Throwable e) {
    if (e instanceof UnhandledException) {
      return (UnhandledException) e;
    }
    return new UnhandledException(e);
  }

  public static Throwable getPeeled(Throwable e) {
    while(e instanceof UnhandledException) {
      e = e.getCause();
    }
    return e;
  }
}
