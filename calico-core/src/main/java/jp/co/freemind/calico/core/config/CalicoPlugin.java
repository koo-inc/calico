package jp.co.freemind.calico.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matcher;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;

public abstract class CalicoPlugin {
  public CalicoPlugin() {
    configure();
  }

  private final List<Module> guiceModules = new ArrayList<>();
  private final List<Consumer<Binder>> globalConsumers = new ArrayList<>();
  private final List<Consumer<Binder>> instantConsumers = new ArrayList<>();
  private final List<InterceptionHandler> handlers = new ArrayList<>();

  Module getGuiceModule() {
    return new GuiceModule(globalConsumers, new ArrayList<>(guiceModules));
  }
  InterceptionHandler[] getInterceptionHandlers() {
    return handlers.stream().toArray(InterceptionHandler[]::new);
  }

  protected abstract void configure();

  protected void install(CalicoPlugin plugin) {
    guiceModules.addAll(plugin.guiceModules);
    globalConsumers.addAll(plugin.globalConsumers);
    instantConsumers.addAll(plugin.instantConsumers);
    handlers.addAll(plugin.handlers);
  }

  protected void install(Module module) {
    guiceModules.add(module);
  }

  protected void bind(Consumer<Binder> consumer) {
    this.globalConsumers.add(consumer);
  }
  protected void bindInstant(Consumer<Binder> consumer) {
    this.instantConsumers.add(consumer);
  }
  protected void intercept(Matcher<Class> typeMatcher, EndpointInterceptor... interceptors) {
    handlers.addAll(getHandlerList(typeMatcher, interceptors));
  }
  protected void interceptBefore(@Nullable Class<? extends EndpointInterceptor> targetInterceptor, Matcher<Class> typeMatcher, EndpointInterceptor... interceptors) {
    int index = targetInterceptor != null ? findIndexOf(targetInterceptor) : 0;
    handlers.addAll(index, getHandlerList(typeMatcher, interceptors));
  }
  protected void interceptAfter(@Nullable Class<? extends EndpointInterceptor> targetInterceptor, Matcher<Class> typeMatcher, EndpointInterceptor... interceptors) {
    int maxIndex = handlers.size() - 1;
    int index = (targetInterceptor != null ? findIndexOf(targetInterceptor) : maxIndex) + 1;
    if (index > maxIndex) {
      handlers.addAll(getHandlerList(typeMatcher, interceptors));
    }
    else {
      handlers.addAll(index, getHandlerList(typeMatcher, interceptors));
    }
  }

  private List<InterceptionHandler> getHandlerList(Matcher<Class> typeMatcher, EndpointInterceptor[] interceptors) {
    return Arrays.stream(interceptors)
      .map(interceptor -> new InterceptionHandler(typeMatcher, interceptor))
      .collect(Collectors.toList());
  }

  private int findIndexOf(Class<? extends EndpointInterceptor> targetInterceptor) {
    for (int i = 0, len = handlers.size(); i < len; i++) {
      if (handlers.get(i).getInterceptorClass().equals(targetInterceptor)) {
        return i;
      }
    }

    throw new IllegalStateException(targetInterceptor.getSimpleName() + " is not found in EndpointInterceptors. current: " + getInterceptorNames() + "");
  }

  private String getInterceptorNames() {
    return this.handlers.stream()
      .map(h -> h.getInterceptorClass().getSimpleName())
      .collect(Collectors.joining(", ", "[", "]"));
  }

  @Override
  public String toString() {
    return "(interceptors: " + getInterceptorNames() + ")";
  }
}
