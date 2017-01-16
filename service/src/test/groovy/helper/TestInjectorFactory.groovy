package helper

import calicosample.core.ServiceModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import jp.co.freemind.calico.guice.InjectorFactory
/**
 * Created by tasuku on 15/04/09.
 */
class TestInjectorFactory extends InjectorFactory {
  @Override
  protected Injector createInjector() {
    return Guice.createInjector(Modules.override(new ServiceModule()).with(new TestServiceModule()))
  }
}
