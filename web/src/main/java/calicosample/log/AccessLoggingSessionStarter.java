package calicosample.log;

import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.log.LoggingSessionStarter;
import jp.co.freemind.calico.core.di.InjectorRef;

public class AccessLoggingSessionStarter implements LoggingSessionStarter {
  @Override
  public LoggingSession start() {
    return InjectorRef.getCurrent().getInstance(AccessLoggingSession.class);
  }
}
