package jp.co.freemind.calico.core.auth;

import jp.co.freemind.calico.core.config.Setting;

@Setting("auth")
public interface AuthSetting {
  String getSecretToken();
}
