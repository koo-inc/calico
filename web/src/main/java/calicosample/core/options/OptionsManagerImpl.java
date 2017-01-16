package calicosample.core.options;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import calicosample.core.options.supplier.ExtEnumOptionsSupplier;
import calicosample.core.options.supplier.JsonFileOptionsSupplier;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class OptionsManagerImpl extends AbstractOptionsManager {
  @Inject
  private ServletContext servletContext;

  @Override
  protected OptionsSupplier[] getSuppliers() {
    return new OptionsSupplier[]{
      new JsonFileOptionsSupplier(servletContext.getRealPath("/mock-data/common/options")),
      new ExtEnumOptionsSupplier()
    };
  }
}
