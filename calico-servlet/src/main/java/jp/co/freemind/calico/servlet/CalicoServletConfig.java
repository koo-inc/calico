package jp.co.freemind.calico.servlet;

import java.sql.DriverManager;
import java.util.Collections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.Logger;

import jp.co.freemind.calico.core.config.CalicoConfig;

public abstract class CalicoServletConfig extends CalicoConfig implements ServletContextListener {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CalicoServletConfig.class);
  public static String INTERCEPTOR_HANDLERS = CalicoServletConfig.class.getCanonicalName() + ".interceptorHandlers";

  @Override
  public void contextInitialized(ServletContextEvent e) {
    initApplication();
    e.getServletContext().setAttribute(INTERCEPTOR_HANDLERS, getInterceptionHandlers());
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    Collections.list(DriverManager.getDrivers()).forEach(driver -> {
      try {
        DriverManager.deregisterDriver(driver);
      } catch (Exception e) {
        log.error(e);
      }
    });
  }
}
