package calicosample.core.mail;

import jp.co.freemind.calico.core.config.Setting;

@Setting("fmmailer")
public interface FmMailerSetting {
  String getApiUrl();
}
