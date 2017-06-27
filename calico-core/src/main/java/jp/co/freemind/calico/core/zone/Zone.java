package jp.co.freemind.calico.core.zone;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.util.Modules;
import jp.co.freemind.calico.core.config.Registry;
import jp.co.freemind.calico.core.util.Throwables;

public class Zone {

  private static Zone root;
  private static ThreadLocal<Zone> currentRef;

  private final Zone parent;
  private final ZoneSpec spec;
  private final Map<Key<?>, Provider<?>> providers;
  private final Injector injector;

  private Zone(Zone parent, ZoneSpec spec) {
    this.parent = parent;
    this.spec = spec;
    this.providers = new ConcurrentHashMap<>(spec.getProviders());
    if (!spec.getModule().isPresent() && parent != null) {
      this.injector = parent.injector;
    }
    else if (parent != null) {
      this.injector = parent.injector.createChildInjector(spec.getModule().orElse(Modules.EMPTY_MODULE));
    }
    else {
      this.injector = Guice.createInjector(spec.getModule().orElse(Modules.EMPTY_MODULE));
    }
  }

  public static synchronized Zone initialize(Function<ZoneSpec, ZoneSpec> specFn) {
    if (root != null) throw new IllegalStateException("Zone is already initialized.");
    currentRef = new ThreadLocal<>();
    ZoneSpec spec = specFn.apply(new ZoneSpec());
    root = new Zone(null, spec);
    return root;
  }

  static synchronized void dispose() {
    if (root == null) throw new IllegalStateException("Zone is not initialized yet.");
    currentRef.remove();
    root = null;
  }

  public static Zone getCurrent() {
    if (root == null) throw new IllegalStateException("Zone is not yet initialized.");
    Zone current = currentRef.get();
    if (current != null) {
      return current;
    }
    return root;
  }

  public static Context getContext() {
    return Zone.getCurrent().getInstance(Context.class);
  }

  public static Registry getRegistry() {
    return Zone.getCurrent().getInstance(Registry.class);
  }

  public Zone fork(Function<ZoneSpec, ZoneSpec> specFn) {
    ZoneSpec spec = specFn.apply(new ZoneSpec());
    return new Zone(this, spec);
  }

  public void run(Processable processable) {
    call(processable.toExecutable());
  }

  @SuppressWarnings("ThrowFromFinallyBlock")
  public <T> Optional<T> call(Executable<T> executable) {
    Zone context = Zone.getCurrent();
    try {
      return Optional.ofNullable(doInZone(executable));
    }
    catch (Throwable t) {
      try {
        propagateThrowable(t, context);
      }
      catch (UnhandledException t2) {
        throw t2;
      }
      catch (Throwable t2) {
        throw new UnhandledException(t);
      }
    }
    finally {
      try {
        doInZone(spec::doFinish);
      } catch (Throwable t) {
        // spec.onFinish は Runnable のため例外を発生しないはず
        throw Throwables.sneakyThrow(t);
      }
    }
    return Optional.empty();
  }

  Optional<Class<? extends Annotation>> getScope() {
    return spec.getScope();
  }

  @SuppressWarnings("unchecked")
  <T> Provider<T> getProvider(Class<? extends Annotation> scope, Key<T> key, Provider<T> unscoped) {
    boolean isInScope = getScope().map(s -> s == scope).orElse(false);
    if (isInScope && providers.containsKey(key)) {
      return (Provider<T>) providers.get(key);
    }
    return parent != null ? parent.getProvider(scope, key, unscoped) : unscoped;
  }

  private void propagateThrowable(Throwable t, Zone context) throws Throwable {
    try {
      Throwable target = t;
      if (doInZone(() -> spec.doOnError(target))) {
        return;
      }
    }
    catch (Throwable t2) {
      t = t2;
    }
    if (parent != null && parent != context) {
      Throwable target = t;
      parent.doInZone(() -> parent.propagateThrowable(target, context));
    }
    else {
      throw new UnhandledException(t);
    }
  }

  private void doInZone(Processable processable) throws Throwable {
    doInZone(processable.toExecutable());
  }

  private <T> T doInZone(Executable<T> executable) throws Throwable {
    Zone prev = null;
    try {
      prev = getCurrent();
      currentRef.set(this);
      return executable.execute();
    }
    finally {
      if (prev != root) {
        currentRef.set(prev);
      }
      else {
        currentRef.remove();
      }
    }
  }

  public Runnable bind(Processable processable) {
    return new ZoneRunnable(this, processable);
  }

  public <V> Callable<Optional<V>> bindWithCallable(Executable<V> executable) {
    return new ZoneCallable<>(this, executable);
  }

  public Injector getInjector() {
    return injector;
  }

  public void injectMembers(Object instance) {
    injector.injectMembers(instance);
  }

  public <T> T getInstance(Key<T> key) {
    return injector.getInstance(key);
  }

  public <T> T getInstance(Class<T> type) {
    return injector.getInstance(Key.get(type));
  }

}
