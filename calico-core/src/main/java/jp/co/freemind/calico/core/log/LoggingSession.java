package jp.co.freemind.calico.core.log;

public interface LoggingSession {
  void finish(int statusCode);
  void finish(Throwable t);
}
