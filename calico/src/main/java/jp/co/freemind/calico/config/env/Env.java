package jp.co.freemind.calico.config.env;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static jp.co.freemind.calico.util.FunctionUtil.silence;

@Log4j2
public class Env {
  @Getter
  private static String envName;
  private static Properties properties;

  public static void init() {
    init(determineEnvName(), Env::loadProperties);
  }
  public static void init(String envName) {
    init(envName, Env::loadProperties);
  }
  public static void init(Properties properties) {
    init(determineEnvName(), () -> {
      Properties props = new Properties();
      try {
        props.putAll(loadProperties());
      } catch (Exception e) {
      }
      props.putAll(properties);
      return props;
    });
  }
  public static void init(String envName, Supplier<Properties> propertiesSupplier) {
    log.info("init as env=" + envName);
    Env.envName = envName;
    Env.properties = propertiesSupplier.get();
  }

  /**
   * init で設定。
   * jvm起動オプションに -Dcalico.env=production のように設定。
   * もしくは環境変数に CALICO_ENV=production のように設定。
   * 優先順位は、init指定 > jvm起動オプション > 環境変数。
   */
  private static String determineEnvName(){
    if (envName != null) return envName;
    if (System.getProperty("calico.env") != null) return System.getProperty("calico.env");
    if (System.getenv("CALICO_ENV") != null) return System.getenv("CALICO_ENV");
    return "development";
  }

  private static Properties loadProperties() {
    PropertiesLoader propertiesLoader = new PropertiesLoader();

    Properties properties = new Properties();
    properties.putAll(propertiesLoader.load("env/.autogen.properties", false));
    properties.putAll(propertiesLoader.load("env/default.properties", true));

    propertiesLoader.addLookupPath(Paths.get(System.getProperty("user.home"), ".calico", properties.getProperty("rootPackage")));

    properties.putAll(propertiesLoader.load("env/" + getEnvName() + ".properties", true));

    if (properties.getProperty("secretPath") != null) {
      Path secretPath = Paths.get(properties.getProperty("secretPath"));
      propertiesLoader.addLookupPath(secretPath.getParent(), true);
      Properties secretProps = propertiesLoader.load(secretPath.getFileName().toString(), true);

      if (secretProps.getProperty("secretToken") == null) {
        throw new SecurityException(secretProps + " に secretToken が定義されていません");
      }
      else if (secretProps.getProperty("secretToken").getBytes().length != 32) {
        throw new SecurityException(secretProps + " の secretToken には32バイトの文字列を定義してください");
      }
      properties.putAll(secretProps);
    }

    return properties;
  }

  public static String getProperty(String key){
    return properties.getProperty(key);
  }

  public static String getProperty(String key, String defaultValue){
    return properties.getProperty(key, defaultValue);
  }

  public static String getRootPackage(){
    return getProperty("rootPackage");
  }

  public static String getInjectorFactory(){
    return getProperty("injectorFactory");
  }

  public static String getFrontResourcePackage() {
    return getProperty("frontResourcePackage");
  }

  public static String getSecretToken() {
    return getProperty("secretToken");
  }

  private static Long deployedAt;
  public static long getDeployedAt() {
    if (deployedAt != null) return deployedAt;
    try {
      deployedAt = Long.parseLong(getProperty("deployedAt"));
    }
    catch (Exception e) {
      log.warn(".autogen.properties が存在しないようです。deployedAt にはクラスロード時間が使われます。");
      deployedAt = System.currentTimeMillis();
    }
    return deployedAt;
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public static <I extends PartialEnv> I loadPartial(Class<I> iface) {
    String envPath = iface.getAnnotation(EnvPath.class).value();

    Map<Method, Object> cache = new HashMap<>();

    Map<Method, PropertyDescriptor> descriptorMap = getInterfacesFromRoot(iface).stream()
      .map(silence(Introspector::getBeanInfo))
      .map(BeanInfo::getPropertyDescriptors)
      .flatMap(Stream::of)
      .collect(Collectors.toMap(PropertyDescriptor::getReadMethod, desc -> desc));

    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }

    return (I) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface }, (proxy, method, args) -> {
      if (cache.containsKey(method)) return cache.get(method);

      PropertyDescriptor descriptor = descriptorMap.get(method);
      Object property = descriptor != null ? getProperty(envPath + "." + descriptor.getName()) : null;
      if (property == null && method.isDefault()) {
        // default メソッドの実行
        // https://rmannibucau.wordpress.com/2014/03/27/java-8-default-interface-methods-and-jdk-dynamic-proxies/
        property = constructor.newInstance(iface, MethodHandles.Lookup.PRIVATE)
          .unreflectSpecial(method, iface)
          .bindTo(proxy)
          .invokeWithArguments(args);
      }
      cache.put(method, property);
      return property;
    });
  }

  // 基本は幅優先探索。同じインターフェースが出現した場合はルートに近いものを優先する。
  private static List<Class<?>> getInterfacesFromRoot(Class<?> iface) {
    List<Class<?>> ifaces = flattenInheritedClasses(iface).collect(Collectors.toList());
    return Lists.reverse(ifaces).stream().distinct().collect(Collectors.toList());
  }

  private static Stream<Class<?>> flattenInheritedClasses(Class<?> iface) {
    return Stream.concat(
      Stream.concat(Stream.of(iface), Stream.of(iface.getInterfaces())),
      Stream.of(iface.getInterfaces()).distinct().flatMap(Env::flattenInheritedClasses)
    );
  }
}
