package helper
import com.google.inject.Inject
import helper.factory.DataFactory
import jp.co.freemind.calico.core.config.env.Env
import jp.co.freemind.calico.guice.InjectUtil
import jp.co.freemind.calico.core.util.ClassFinder
import org.seasar.doma.jdbc.Config
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
/**
 * Created by tasuku on 15/04/13.
 */
class ServiceTestInterceptor extends AbstractMethodInterceptor {
  @Inject
  Config config;

  ServiceTestInterceptor() {
    Env.init System.getenv("CALICO_TEST_ENV") ?: System.getProperty("CALICO_TEST_ENV") ?: "test"
    InjectUtil.injectMembers(this)
    DataFactory.configure {
      dataSource config.dataSource
      ClassFinder.findClasses("factory").each {
        resister it
      }
    }
  }

  @Override
  void interceptFeatureMethod(IMethodInvocation invocation) throws Throwable {
    DataFactory.resetAll()
    config.transactionManager.required({
      config.transactionManager.setRollbackOnly()
      if (setupInvocation != null) setupInvocation.proceed()
      invocation.proceed()
    })
  }

  @Override
  void interceptCleanupMethod(IMethodInvocation invocation) {
    invocation.proceed()
    ContextHelper.reset()
  }

  def setupInvocation
  def void interceptSetupMethod(IMethodInvocation invocation) {
    setupInvocation = invocation
  }
}
