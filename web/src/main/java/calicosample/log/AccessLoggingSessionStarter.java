package calicosample.log;

import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.log.LoggingSessionStarter;
import jp.co.freemind.calico.core.zone.Zone;

public class AccessLoggingSessionStarter implements LoggingSessionStarter {
  @Override
  public LoggingSession start() {
    return Zone.getCurrent().getInstance(AccessLoggingSession.class);
  }
}
