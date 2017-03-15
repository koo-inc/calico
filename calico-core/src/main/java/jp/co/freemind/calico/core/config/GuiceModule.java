package jp.co.freemind.calico.core.config;

import java.util.List;
import java.util.function.Consumer;

import com.google.inject.Binder;
import com.google.inject.Module;

class GuiceModule implements Module {
  private final List<Consumer<Binder>> binders;
  private final List<Module> modules;

  GuiceModule(List<Consumer<Binder>> binders, List<Module> modules) {
    this.binders = binders;
    this.modules = modules;
  }

  @Override
  public void configure(Binder binder) {
    modules.forEach(binder::install);
    binders.forEach(c -> c.accept(binder));
  }
}
