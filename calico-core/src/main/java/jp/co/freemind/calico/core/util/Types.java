package jp.co.freemind.calico.core.util;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.freemind.calico.core.config.VersionSpecificMethods;

public final class Types {
  private static Map<String, List<Package>> packageCache = new ConcurrentHashMap<>();
  public static List<Package> getAncestorPackages(Class<?> type) {
    return packageCache.computeIfAbsent(type.getName(), key -> {
      List<Package> packages = new ArrayList<>();
      String[] fragments = type.getName().split("\\.");
      for (int n = fragments.length - 1; n > 0; n--) {
        String pkgName = stream(copyOf(fragments, n)).collect(joining("."));
        Package pkg = VersionSpecificMethods.INSTANCE.getDefinedPackage(Types.class.getClassLoader(), pkgName);
        if (pkg == null) continue;

        packages.add(pkg);
      }
      return Collections.unmodifiableList(packages);
    });
  }
}
