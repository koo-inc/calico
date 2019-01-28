package jp.co.freemind.calico.core.config;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface VersionSpecificMethods {

  <T> MethodHandles.Lookup privateLookupIn(Class<T> iface);

  Package getDefinedPackage(ClassLoader classLoader, String packageName);

  VersionSpecificMethods INSTANCE = build();

  static VersionSpecificMethods build() {
    String versionStr = System.getProperty("java.version");
    int pos = versionStr.indexOf(".", versionStr.indexOf(".") + 1);
    double version = Double.parseDouble(versionStr.substring(0, pos));

    if (version < 9) {
      return new Jvm8VersionSpecificMethods();
    }
    else {
      return new Jvm9VersionSpecificMethods();
    }
  }

  class Jvm9VersionSpecificMethods implements VersionSpecificMethods {
    private final Method privateLookupIn;
    private final Method getDefinedPackage;

    Jvm9VersionSpecificMethods() {
      try {
        this.privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        this.getDefinedPackage = ClassLoader.class.getMethod("getDefinedPackage", String.class);
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public <T> MethodHandles.Lookup privateLookupIn(Class<T> iface) {
      try {
        return (MethodHandles.Lookup) privateLookupIn.invoke(null, iface, MethodHandles.lookup());
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public Package getDefinedPackage(ClassLoader classLoader, String packageName) {
      try {
        return (Package) getDefinedPackage.invoke(classLoader, packageName);
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  class Jvm8VersionSpecificMethods implements VersionSpecificMethods {
    @Override
    public <T> MethodHandles.Lookup privateLookupIn(Class<T> iface) {
      final Constructor<MethodHandles.Lookup> constructor = getConstructor();
      try {
        return constructor.newInstance(iface, MethodHandles.Lookup.PRIVATE);
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public Package getDefinedPackage(ClassLoader classLoader, String packageName) {
      return Package.getPackage(packageName);
    }

    private Constructor<MethodHandles.Lookup> getConstructor() {
      final Constructor<MethodHandles.Lookup> constructor;
      try {
        constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor;
    }
  }
}
