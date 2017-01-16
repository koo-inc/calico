package jp.co.freemind.calico.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.co.freemind.calico.core.config.CalicoConfig;

public abstract class CalicoServletConfig extends CalicoConfig implements ServletContextListener {
  public static String INTERCEPTOR_HANDLERS = CalicoServletConfig.class.getCanonicalName() + ".interceptorHandlers";
  @Override
  public void contextInitialized(ServletContextEvent e) {
    initApplication();
    e.getServletContext().setAttribute(INTERCEPTOR_HANDLERS, getInterceptionHandlers());
  }

  @Override
  public void contextDestroyed(ServletContextEvent e) {
  }
}
