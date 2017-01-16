package calicosample;

import calicosample.auth.CalicoSampleAuthority;
import calicosample.core.ServicePlugin;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.options.OptionsManager;
import calicosample.core.options.OptionsManagerImpl;
import calicosample.entity.log.AccessStartLog;
import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.interceptor.ResultMapper;
import jp.co.freemind.calico.core.endpoint.interceptor.TransactionInterceptor;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.CalicoServletPlugin;
import jp.co.freemind.calico.servlet.CertificateAuthority;
import jp.co.freemind.calico.servlet.DefaultResultRenderer;
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
      new ResultMapper(new DefaultResultRenderer())
    );

    bind(binder -> {
      binder.bind(CertificateAuthority.class).to(CalicoSampleAuthority.class);
    });
    bindInstant(binder -> {
      binder.bind(OptionsManager.class).to(OptionsManagerImpl.class);
      binder.bind(AccessStartLog.class).in(TransactionScoped.class);
    });
  }

  private static CalicoSampleAuthInfo getNullAuthInfo() {
    Context context = Zone.getContext();
    return CalicoSampleAuthInfo.ofNull(context.getRemoteAddress(), context.getProcessDateTime());
  }
}
