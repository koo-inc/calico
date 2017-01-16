package jp.co.freemind.calico.core.zone;

public class UnhandledException extends RuntimeException {
  public UnhandledException(Throwable e) {
    super(e.getMessage(), e);
  }
}
