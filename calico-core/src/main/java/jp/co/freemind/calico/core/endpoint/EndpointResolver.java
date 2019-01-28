package jp.co.freemind.calico.core.endpoint;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.config.VersionSpecificMethods;

@Singleton
public class EndpointResolver {
  private final SystemSetting systemSetting;
  private final Map<String, Optional<EndpointInfo>> cache;

  @Inject
  public EndpointResolver(SystemSetting systemSetting) {
    this.systemSetting = systemSetting;
    this.cache = new ConcurrentHashMap<>();
  }

  public Optional<EndpointInfo> resolve(String key) {
    return cache.computeIfAbsent(key, k -> resolveClass(k).map(EndpointInfo::new));
  }

  private static Pattern FRAGMENT = Pattern.compile("^[A-Za-z][_A-Za-z0-9]*");

  @SuppressWarnings("unchecked")
  protected Optional<Class<? extends Endpoint<?, ?>>> resolveClass(String path){
    path = normalizePath(path);
    try {
      String[] fragments = path.split("/+");
      if (fragments.length == 0) return Optional.empty();

      StringBuilder builder = new StringBuilder(systemSetting.getRootPackage()).append(".").append(fragments[0]);

      Package rootPackage = VersionSpecificMethods.INSTANCE.getDefinedPackage(getClass().getClassLoader(), builder.toString());
      if (rootPackage == null) return Optional.empty();
      if (!rootPackage.isAnnotationPresent(EndpointRoot.class)) return Optional.empty();

      for (int i = 1, len = fragments.length - 1; i < len; i++) {
        String fragment = fragments[i];
        if (!FRAGMENT.matcher(fragment).matches()) return Optional.empty();
        builder.append('.').append(fragment.replace("_", ""));
      }
      String fragment = fragments[fragments.length - 1];
      if (!FRAGMENT.matcher(fragment).matches()) return Optional.empty();
      builder.append('.').append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, fragment.toLowerCase())).append("Endpoint");

      Class<? extends Endpoint<?, ?>> endpointClass
        = (Class<? extends Endpoint<?, ?>>) Class.forName(builder.toString(), false, this.getClass().getClassLoader());

      if (Modifier.isAbstract(endpointClass.getModifiers())) return Optional.empty();
      return Optional.of(endpointClass);
    } catch (ClassNotFoundException e) {
      return Optional.empty();
    }
  }

  private String normalizePath(String path) {
    return path.replaceFirst("^/api/", "/").replaceAll("(^/+|/+$)", "").replaceAll("/+", "/");
  }

}
