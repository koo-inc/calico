package calicosample;

import calicosample.auth.CalicoSampleAuthNProcedure;
import calicosample.core.ServicePlugin;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.entity.log.AccessStartLog;
import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.interceptor.ResultMapper;
import jp.co.freemind.calico.core.endpoint.interceptor.TransactionInterceptor;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.CalicoServletPlugin;
import jp.co.freemind.calico.servlet.AuthenticationProcedure;
import jp.co.freemind.calico.servlet.session.TimeoutInterceptor;

public class WebPlugin extends CalicoServletPlugin {
  private static String ROOT_PACKAGE = "calicosample.endpoint";

  @Override
  protected void configure() {
    super.configure();

    install(new ServicePlugin());

    interceptAfter(
      TransactionInterceptor.class,
      Matchers.inSubpackage(ROOT_PACKAGE),
      new TimeoutInterceptor(WebPlugin::getNullAuthInfo),
      new ResultMapper()
    );

    bind(binder -> {
      binder.bind(AuthenticationProcedure.class).to(CalicoSampleAuthNProcedure.class);
    });
    bindInstant(binder -> {
      binder.bind(AccessStartLog.class).in(TransactionScoped.class);
    });
  }

  private static CalicoSampleAuthInfo getNullAuthInfo() {
    Context context = Zone.getContext();
    return CalicoSampleAuthInfo.ofNull(context.getRemoteAddress(), context.getProcessDateTime());
  }
}
