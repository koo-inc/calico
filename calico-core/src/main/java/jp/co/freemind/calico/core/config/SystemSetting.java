package jp.co.freemind.calico.core.config;

import lombok.extern.log4j.Log4j2;

@Setting("system")
public interface SystemSetting {
  String getEnvName();
  String getRootPackage();
  String getDeployedAt();

  default long deployedAt() {
    if (Holder.deployedAt != null) return Holder.deployedAt;
    try {
      Holder.deployedAt = Long.parseLong(getDeployedAt());
    }
    catch (Exception e) {
      Holder.log.warn(".autogen.properties が存在しないようです。deployedAt にはクラスロード時間が使われます。");
      Holder.deployedAt = System.currentTimeMillis();
    }
    return Holder.deployedAt;
  }

  @Log4j2
  final class Holder {
    static Long deployedAt;
  }
}
