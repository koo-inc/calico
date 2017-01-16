package calicosample;

import javax.servlet.annotation.WebListener;

import jp.co.freemind.calico.core.config.Registry;
import jp.co.freemind.calico.servlet.CalicoServletConfig;

@WebListener
public class Config extends CalicoServletConfig {
  @Override
  protected Registry createRegistry() {
    return new Registry("env/calico.properties");
  }

  @Override
  protected void configure(Registry registry) {
    this.addPlugin(new WebPlugin());
  }
}
