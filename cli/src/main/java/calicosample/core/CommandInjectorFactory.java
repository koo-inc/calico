package calicosample.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;
import calicosample.core.auth.CalicoSampleAuthInfo;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.context.ContextProxy;
import jp.co.freemind.calico.guice.InjectorFactory;

/**
 * Created by kakusuke on 15/07/16.
 */
public class CommandInjectorFactory extends InjectorFactory {
  @Override
  protected Injector createInjector() {
    return Guice.createInjector(new AbstractModule() {
      @Override protected void configure() {
        install(new ServiceModule());

        bind(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {})
          .to(new TypeLiteral<ContextProxy<CalicoSampleAuthInfo, CommandContext>>() {
          });

        SimpleScope requestScope = new SimpleScope();
        bindScope(RequestScoped.class, requestScope);
        bind(SimpleScope.class).annotatedWith(Names.named("requestScope")).toInstance(requestScope);

        SimpleScope sessionScope = new SimpleScope();
        bindScope(SessionScoped.class, sessionScope);
        bind(SimpleScope.class).annotatedWith(Names.named("sessionScope")).toInstance(sessionScope);
      }
    });
  }
}
