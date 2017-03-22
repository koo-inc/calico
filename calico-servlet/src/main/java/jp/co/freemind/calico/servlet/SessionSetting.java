package jp.co.freemind.calico.servlet;

import jp.co.freemind.calico.core.config.Setting;

@Setting("session")
public interface SessionSetting {
  String getTokenKey();
  String getCsrfTokenKey();
  String getTimeoutMinutes();

  default long timeoutSecond() {
    return Long.parseLong(getTimeoutMinutes()) * 60;
  }
}
