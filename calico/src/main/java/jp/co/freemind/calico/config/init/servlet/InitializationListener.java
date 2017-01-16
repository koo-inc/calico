package jp.co.freemind.calico.config.init.servlet;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import jp.co.freemind.calico.config.init.ApplicationInitializer;
import jp.co.freemind.calico.guice.InjectUtil;

import javax.servlet.annotation.WebListener;

@WebListener
public class InitializationListener extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    ApplicationInitializer.init();
    return InjectUtil.getInjector();
  }
}

