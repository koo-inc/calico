package jp.co.freemind.calico.core.di;

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

public class InjectorRef {

  private static InjectorRef root;
  private static ThreadLocal<InjectorRef> currentRef;

  private final InjectorRef parent;
  private final InjectorSpec spec;
  private final Map<Key<?>, Provider<?>> providers;
  private final Injector injector;

  private InjectorRef(InjectorRef parent, InjectorSpec spec) {
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

  public static synchronized InjectorRef initialize(Function<InjectorSpec, InjectorSpec> specFn) {
    if (root != null) throw new IllegalStateException("InjectorRef is already initialized.");
    currentRef = new ThreadLocal<>();
    InjectorSpec spec = specFn.apply(new InjectorSpec());
    root = new InjectorRef(null, spec);
    return root;
  }

  static synchronized void dispose() {
    if (root == null) throw new IllegalStateException("InjectorRef is not initialized yet.");
    currentRef.remove();
    root = null;
  }

  public static InjectorRef getCurrent() {
    if (root == null) throw new IllegalStateException("InjectorRef is not yet initialized.");
    InjectorRef current = currentRef.get();
    if (current != null) {
      return current;
    }
    return root;
  }

  public static Context getContext() {
    return InjectorRef.getCurrent().getInstance(Context.class);
  }

  public static Registry getRegistry() {
    return InjectorRef.getCurrent().getInstance(Registry.class);
  }

  public InjectorRef fork(Function<InjectorSpec, InjectorSpec> specFn) {
    InjectorSpec spec = specFn.apply(new InjectorSpec());
    return new InjectorRef(this, spec);
  }

  public void run(Processable processable) {
    call(processable.toExecutable());
  }

  @SuppressWarnings("ThrowFromFinallyBlock")
  public <T> Optional<T> call(Executable<T> executable) {
    InjectorRef context = InjectorRef.getCurrent();
    try {
      return Optional.ofNullable(doInZone(executable));
    }
    catch (Throwable t) {
      try {
        propagateThrowable(t, context);
      }
      catch (Throwable t2) {
        throw Throwables.sneakyThrow(t2);
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

  private void propagateThrowable(Throwable t, InjectorRef context) throws Throwable {
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
      throw UnhandledException.of(t);
    }
  }

  private void doInZone(Processable processable) throws Throwable {
    doInZone(processable.toExecutable());
  }

  private <T> T doInZone(Executable<T> executable) throws Throwable {
    InjectorRef prev = null;
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
    return new RunnableBinder(this, processable);
  }

  public <V> Callable<Optional<V>> bindWithCallable(Executable<V> executable) {
    return new CallableBinder<>(this, executable);
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
