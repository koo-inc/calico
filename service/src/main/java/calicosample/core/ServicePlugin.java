package calicosample.core;

import com.google.inject.matcher.Matchers;
import jp.co.freemind.calico.core.config.CalicoPlugin;
import jp.co.freemind.calico.core.endpoint.interceptor.AuthorizationInterceptor;
import jp.co.freemind.calico.core.endpoint.interceptor.TransactionInterceptor;
import jp.co.freemind.calico.core.endpoint.interceptor.ValidationInterceptor;
import jp.co.freemind.calico.core.validation.VerificationExceptionMapper;

public class ServicePlugin extends CalicoPlugin {

  @Override
  protected void configure() {
    install(new ServiceModule());
    intercept(
      Matchers.inSubpackage("calicosample.endpoint"),
      new TransactionInterceptor(),
      new VerificationExceptionMapper(),
      new AuthorizationInterceptor(),
      new ValidationInterceptor()
    );
  }
}
