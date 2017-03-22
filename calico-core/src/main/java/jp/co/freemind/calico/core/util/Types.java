package jp.co.freemind.calico.core.util;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

public interface Types {
  static List<Package> getAncestorPackages(Class<?> type) {
    List<Package> packages = new ArrayList<>();
    String[] fragments = type.getName().split("\\.");
    for (int n = fragments.length - 1; n > 0; n--) {
      String pkgName = stream(copyOf(fragments, n)).collect(joining("."));
      Package pkg = Package.getPackage(pkgName);
      if (pkg == null) continue;

      packages.add(pkg);
    }
    return packages;
  }
}
