package jp.co.freemind.calico.core.zone;

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
}
