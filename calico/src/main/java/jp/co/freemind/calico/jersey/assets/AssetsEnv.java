package jp.co.freemind.calico.jersey.assets;

import jp.co.freemind.calico.config.env.EnvPath;
import jp.co.freemind.calico.config.env.PartialEnv;

/**
 * Created by kakusuke on 15/05/22.
 */
@EnvPath("assets")
public interface AssetsEnv extends PartialEnv {
  String getCacheEnabled();

  default boolean cacheEnabled() {
    return Boolean.valueOf(getCacheEnabled());
  }
}
