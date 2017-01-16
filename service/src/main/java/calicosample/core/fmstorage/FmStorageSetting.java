package calicosample.core.fmstorage;

import jp.co.freemind.calico.core.config.Setting;

@Setting("fmstorage")
public interface FmStorageSetting {
  String getUrl();
  String getBasePath();
  default String getCharset() {
    return "UTF-8";
  }
}
