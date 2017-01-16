package jp.co.freemind.calico.servlet.assets;

import jp.co.freemind.calico.core.config.Setting;

@Setting("assets")
public interface AssetsSetting {
  String getCacheEnabled();
  default String getBaseDir() { return "/assets"; }

  default boolean cacheEnabled() {
    return Boolean.valueOf(getCacheEnabled());
  }

  default String getIndex() { return "index.html"; }
}
