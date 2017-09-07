package jp.co.freemind.calico.core.log;

public interface LoggingSession<ID> {
  void finish(int statusCode);
  void error(Throwable t);
  ID getId();
}
