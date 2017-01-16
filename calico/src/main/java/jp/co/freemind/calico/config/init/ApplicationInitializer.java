package jp.co.freemind.calico.config.init;

import jp.co.freemind.calico.config.beanutils.SuppressPropertiesInitializer;
import jp.co.freemind.calico.config.env.Env;

public class ApplicationInitializer {
  public static void init(){
    SuppressPropertiesInitializer.init();
    Env.init();
  }
}
