package jp.co.freemind.calico.core.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class Registry {
  private final Map<String, String> settings;

  public Registry(String propertyFile) {
    settings = loadSettings(propertyFile);
  }

  @SuppressWarnings("unchecked")
  @Deprecated
  public Registry(Properties properties) {
    settings = (Map<String, String>) Collections.unmodifiableMap((Map) properties);
  }


  @SuppressWarnings("unchecked")
  private static Map<String, String> loadSettings(String propertyFile) {
    Properties properties = new Properties();

    try (InputStream is = Resources.getResource(propertyFile).openStream();
         InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)) {
      properties.load(isr);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    properties.putAll(getenv());
    properties.putAll(System.getProperties());

    return (Map<String, String>) Collections.unmodifiableMap((Map) properties);
  }

  public String getValue(String key) {
    String value = settings.get(key);
    if (value == null) {
      throw new NullPointerException("undefined property key '" + key + "'");
    }
    return value;
  }
  public String getValue(String key, String defaultValue) {
    String value = settings.get(key);
    return value != null ? value : defaultValue;
  }

  @SuppressWarnings("unchecked")
  public <I> I loadSetting(Class<I> iface) {
    String settingPath = iface.getAnnotation(Setting.class).value();

    Map<Method, Object> cache = new HashMap<>();

    Map<Method, PropertyDescriptor> descriptorMap = getInterfacesFromRoot(iface).stream()
      .map(toBeanInfo())
      .map(BeanInfo::getPropertyDescriptors)
      .flatMap(Stream::of)
      .collect(Collectors.toMap(PropertyDescriptor::getReadMethod, desc -> desc));

    return (I) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface }, (proxy, method, args) -> {
      if (cache.containsKey(method)) return cache.get(method);

      Object property;
      if (method.isDefault()) {
        // default メソッドの実行
        // https://rmannibucau.wordpress.com/2014/03/27/java-8-default-interface-methods-and-jdk-dynamic-proxies/
        // http://mail.openjdk.java.net/pipermail/jigsaw-dev/2017-January/010741.html
        property = MethodHandles.privateLookupIn(iface, MethodHandles.lookup())
          .unreflectSpecial(method, iface)
          .bindTo(proxy)
          .invokeWithArguments(args);
      } else {
        property = getValue(settingPath, descriptorMap.get(method), method);
      }
      cache.put(method, property);
      return property;
    });
  }

  protected Object getValue(String settingPath, PropertyDescriptor descriptor, Method method) {
    if (descriptor == null ) return null;

    if (method.isAnnotationPresent(Nullable.class)) {
      return Registry.this.getValue(settingPath + "." + descriptor.getName(), null);
    }
    else {
      return Registry.this.getValue(settingPath + "." + descriptor.getName());
    }
  }

  private Function<Class<?>, BeanInfo> toBeanInfo() {
    return d -> {
      try {
        return Introspector.getBeanInfo(d);
      } catch (IntrospectionException e) {
        throw new RuntimeException(e);
      }
    };
  }

  // 基本は幅優先探索。同じインターフェースが出現した場合はルートに近いものを優先する。
  private static List<Class<?>> getInterfacesFromRoot(Class<?> iface) {
    List<Class<?>> ifaces = flattenInheritedClasses(iface).collect(Collectors.toList());
    return Lists.reverse(ifaces).stream().distinct().collect(Collectors.toList());
  }
  private static Stream<Class<?>> flattenInheritedClasses(Class<?> iface) {
    return Stream.concat(
      Stream.concat(Stream.of(iface), Stream.of(iface.getInterfaces())),
      Stream.of(iface.getInterfaces()).distinct().flatMap(Registry::flattenInheritedClasses)
    );
  }

  private static Map<String, String> getenv() {
    return System.getenv();
  }
}
