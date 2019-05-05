package jp.co.freemind.calico.core.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

@Setting("system")
public interface SystemSetting {
  String getEnvName();
  String getRootPackage();
  String getVersion();
  String getVersionTag();
  String getProxies();

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

  default List<String> proxies() {
    try {
      String proxies = getProxies();
      Holder.proxies = Arrays.stream(proxies.split("\\s*,\\s*"))
        .map(Strings::trimToNull)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }
    catch (Exception e) {
      Holder.proxies = Collections.emptyList();
    }
    return Holder.proxies;
  }

  final class Holder {
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Holder.class);
    private static Long version;
    private static List<String> proxies;
  }
}
