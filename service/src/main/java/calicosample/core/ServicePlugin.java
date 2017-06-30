package calicosample.core;

import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.config.CalicoPlugin;
import jp.co.freemind.calico.core.endpoint.interceptor.AuthorizationInterceptor;
import jp.co.freemind.calico.core.endpoint.interceptor.ValidationInterceptor;

public class ServicePlugin extends CalicoPlugin {

  @Override
  protected void configure() {
    install(new ServiceModule());
    intercept(
      Matchers.inSubpackage("calicosample.endpoint"),
      new AuthorizationInterceptor(),
      new ValidationInterceptor()
    );
  }
}
