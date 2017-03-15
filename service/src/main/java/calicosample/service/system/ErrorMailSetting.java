package calicosample.service.system;

import com.google.common.base.Strings;
import jp.co.freemind.calico.core.config.Setting;

@Setting("errormail")
public interface ErrorMailSetting {
  String getActive();
  String getReceivers();
  String getSender();
  String getSenderName();

  default boolean active() {
    return !Strings.isNullOrEmpty(getActive()) && Boolean.valueOf(getActive());
  }
}
