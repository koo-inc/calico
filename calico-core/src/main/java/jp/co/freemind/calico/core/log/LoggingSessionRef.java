package jp.co.freemind.calico.core.log;

public class LoggingSessionRef<T> implements LoggingSession<T> {
  private LoggingSession<T> loggingSession = null;

  @Override
  public void finish(int statusCode) {
    ensureDelegatable();
    loggingSession.finish(statusCode);
  }

  @Override
  public void error(Throwable t) {
    ensureDelegatable();
    loggingSession.error(t);
  }

  @Override
  public T getId() {
    ensureDelegatable();
    return loggingSession.getId();
  }

  public void set(LoggingSession<T> loggingSession) {
    if (this.loggingSession != null) {
      throw new IllegalStateException("LoggingSessionRef is initialized twice.");
    }
    this.loggingSession = loggingSession;
  }

  private void ensureDelegatable() {
    if (loggingSession == null) {
      throw new IllegalStateException("LoggingSessionRef is not initialized yet.");
    }
  }
}
