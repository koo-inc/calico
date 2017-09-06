package jp.co.freemind.calico.mail;

import jp.co.freemind.calico.core.config.Setting;

@Setting("fmmail")
public interface FmMailSetting {
  String getApiUrl();
}
