package jp.co.freemind.calico.guice;

import com.google.common.base.Throwables;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import jp.co.freemind.calico.config.env.Env;
import lombok.AccessLevel;
import lombok.Getter;

public class InjectUtil {
  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private static final InjectorFactory injectorFactory = buildInjectorFactory();
  private static InjectorFactory buildInjectorFactory() {
    try {
      return (InjectorFactory) Class.forName(Env.getInjectorFactory()).newInstance();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static Injector getInjector(){
    return getInjectorFactory().getInjector();
  }

  public static <T> T injectMembers(T instance){
    getInjector().injectMembers(instance);
    return instance;
  }

  public static <T> T getInstance(Class<T> clazz) {
    return getInjector().getInstance(clazz);
  }

  public static <T> T getInstance(TypeLiteral<T> typeLiteral) {
    return getInjector().getInstance(Key.get(typeLiteral));
  }
}
