package jp.co.freemind.calico.core.di;

import java.util.Optional;
import java.util.function.Function;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import jp.co.freemind.calico.core.config.Registry;

public class InjectorRef {

  private static final ThreadLocal<InjectorRef> injectorRef = new ThreadLocal<>();

  private static Injector root;
  private final Injector injector;

  InjectorRef(Injector parent, InjectorSpec spec) {
    ensureInitialized();

    if (parent == null) {
      parent = root;
    }

    Optional<Module> module = spec.buildModule();
    if (!module.isPresent() && parent != null) {
      injector = parent;
    } else {
      injector = parent.createChildInjector(module.orElse(Modules.EMPTY_MODULE));
    }
  }

  public static synchronized void initialize(Function<InjectorSpec, InjectorSpec> specFn) {
    if (root != null) throw new IllegalStateException("InjectorManager is already initialized.");
    InjectorSpec spec = specFn.apply(new InjectorSpec());
    root = Guice.createInjector(spec.buildModule().orElse(Modules.EMPTY_MODULE));
  }

  public static void dispose() {
    if (injectorRef.get() == null) throw new IllegalStateException("InjectorRef is not initialized yet.");
    injectorRef.remove();
  }

  public static InjectorRef get() {
    if (injectorRef.get() == null) {
      injectorRef.remove();
      return new InjectorRef(root, new InjectorSpec());
    }
    return injectorRef.get();
  }

  public static Context getContext() {
    return get().injector.getInstance(Context.class);
  }

  public static Registry getRegistry() {
    return get().injector.getInstance(Registry.class);
  }

  public static void injectMembers(Object instance) {
    get().injector.injectMembers(instance);
  }

  public static <T> T getInstance(Key<T> key) {
    return get().injector.getInstance(key);
  }

  public static <T> T getInstance(Class<T> type) {
    return get().injector.getInstance(Key.get(type));
  }

  public static void doInNewInjector(RunnableWithThrowable runnable) {
    doInNewInjector(s -> s, runnable);
  }

  public static void doInNewInjector(Function<InjectorSpec, InjectorSpec> fun, RunnableWithThrowable runnable) {
    InjectorSpec spec = fun.apply(new InjectorSpec());
    InjectorRef old = injectorRef.get();
    if (old == null && root == null) {
      throw new IllegalStateException("InjectorRef is not initialized yet.");
    }

    try {
      InjectorRef ref = new InjectorRef(old != null ? old.injector : null, spec);
      injectorRef.set(ref);
      runnable.run();
    } catch (Throwable e) {
      throw UnhandledException.of(e);
    } finally {
      if (old != null) {
        injectorRef.set(old);
      } else {
        injectorRef.remove();
      }
    }
  }

  private static void ensureInitialized() {
    if (root == null) {
      throw new IllegalStateException("InjectorRef.root is not initialized yet.");
    }
  }

  @FunctionalInterface
  public interface RunnableWithThrowable {
    void run() throws Throwable;
  }
}
