package jp.co.freemind.calico.core.config;

import lombok.extern.log4j.Log4j2;

@Setting("system")
public interface SystemSetting {
  String getEnvName();
  String getRootPackage();
  String getVersion();
  String getVersionTag();

  default long version() {
    if (Holder.version != null) return Holder.version;
    try {
      Holder.version = Long.parseLong(getVersion());
    }
    catch (Exception e) {
      Holder.log.warn("system.version がプロパティに存在しないようです。version にはクラスロード時間が使われます。");
      Holder.version = System.currentTimeMillis();
    }
    return Holder.version;
  }

  @Log4j2
  final class Holder {
    static Long version;
  }
}
