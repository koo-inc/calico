package calicosample.api.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;
import calicosample.core.options.OptionsForm;
import calicosample.core.options.OptionsManager;
import calicosample.core.options.OptionsRecord;


@Path("options")
@NoAuth
public class OptionsApiResource extends ApiResource {
  @Inject
  OptionsManager optionsManager;

  @POST
  public List<OptionsRecord> get(List<OptionsForm> forms) {
    return optionsManager.get(forms);
  }
}
