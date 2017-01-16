package helper
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo
/**
 * Created by tasuku on 15/04/14.
 */
class ServiceTestExtension extends AbstractAnnotationDrivenExtension<ServiceTest> {
  def interceptor = new ServiceTestInterceptor()

  @Override
  void visitSpecAnnotation(ServiceTest annotation, SpecInfo spec) {
    spec.addSetupInterceptor(interceptor)
    spec.addCleanupInterceptor(interceptor)
    spec.features.each {
      it.featureMethod.addInterceptor(interceptor)
    }
  }
}
