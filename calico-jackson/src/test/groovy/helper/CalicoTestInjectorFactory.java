package helper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import helper.media.MockStorage;
import jp.co.freemind.calico.guice.InjectorFactory;
import jp.co.freemind.calico.core.media.MediaStorage;

/**
 * Created by kakusuke on 15/05/10.
 */
public class CalicoTestInjectorFactory extends InjectorFactory {
  @Override
  protected Injector createInjector() {
    return Guice.createInjector((Module) binder ->
      binder.bind(MediaStorage.class).to(MockStorage.class));
  }
}
