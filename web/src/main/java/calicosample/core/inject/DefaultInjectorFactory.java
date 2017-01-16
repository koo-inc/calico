package calicosample.core.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.context.ContextProxy;
import calicosample.core.ServiceModule;
import calicosample.core.WebContext;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.options.OptionsManager;
import calicosample.core.options.OptionsManagerImpl;
import calicosample.entity.log.AccessStartLog;
import jp.co.freemind.calico.guice.InjectorFactory;

public class DefaultInjectorFactory extends InjectorFactory {

  @Override
  protected Injector createInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        install(new ServiceModule());

        bind(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {})
          .to(new TypeLiteral<ContextProxy<CalicoSampleAuthInfo, WebContext>>() {});
        bind(AccessStartLog.class).in(RequestScoped.class);
        bind(OptionsManager.class).to(OptionsManagerImpl.class);

//        requestStaticInjection(SystemParam.class);
      }
    });
  }
}
