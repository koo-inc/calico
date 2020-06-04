package jp.co.freemind.calico.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.google.inject.Module;

import jp.co.freemind.calico.core.di.InjectorRef;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;

public abstract class CalicoConfig {
  private final List<CalicoPlugin> plugins = new ArrayList<>();
  private Registry registry;

  abstract protected Registry createRegistry();
  abstract protected void configure(Registry registry);

  protected void addPlugin(CalicoPlugin plugin) {
    plugins.add(plugin);
  }

  protected void initApplication() {
    registry = createRegistry();
    plugins.clear();
    configure(registry);
    InjectorRef.initialize(s -> s.modules(getModules()));
  }

  protected InterceptionHandler[] getInterceptionHandlers() {
    return plugins.stream()
      .map(CalicoPlugin::getInterceptionHandlers)
      .flatMap(Arrays::stream)
      .toArray(InterceptionHandler[]::new);
  }

  private Module[] getModules() {
    return Stream.concat(
      plugins.stream().map(CalicoPlugin::getGuiceModule),
      Stream.of(binder -> binder.bind(Registry.class).toInstance(registry))
    ).toArray(Module[]::new);
  }
}
