package calicosample;

import calicosample.auth.CalicoSampleAuthNProcedure;
import calicosample.core.ServicePlugin;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.log.AccessLoggingSession;
import calicosample.log.AccessLoggingSessionStarter;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.interceptor.ResultMapper;
import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.log.LoggingSessionStarter;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.AuthenticationProcedure;
import jp.co.freemind.calico.servlet.CalicoServletPlugin;
import jp.co.freemind.calico.servlet.interceptor.CsrfInterceptor;
import jp.co.freemind.calico.servlet.interceptor.TimeoutInterceptor;
import jp.co.freemind.calico.servlet.interceptor.VersioningInterceptor;

public class WebPlugin extends CalicoServletPlugin {
  private static String ROOT_PACKAGE = "calicosample.endpoint";

  @Override
  protected void configure() {
    super.configure();

    install(new ServicePlugin());

    interceptBefore(null,
      Matchers.inSubpackage(ROOT_PACKAGE),
      new VersioningInterceptor()
    );

    interceptAfter(null,
      Matchers.inSubpackage(ROOT_PACKAGE),
      new CsrfInterceptor(Messages.CSRF),
      new TimeoutInterceptor(WebPlugin::getNullAuthInfo),
      new ResultMapper()
    );

    bind(binder -> {
      binder.bind(AuthenticationProcedure.class).to(CalicoSampleAuthNProcedure.class);
      binder.bind(LoggingSessionStarter.class).to(AccessLoggingSessionStarter.class).in(Singleton.class);
    });

    bindInstant(binder -> {
      binder.bind(LoggingSession.class).to(AccessLoggingSession.class).in(TransactionScoped.class);
    });
  }

  private static CalicoSampleAuthInfo getNullAuthInfo() {
    Context context = Zone.getContext();
    return CalicoSampleAuthInfo.ofNull(context.getRemoteAddress(), context.getProcessDateTime());
  }
}
