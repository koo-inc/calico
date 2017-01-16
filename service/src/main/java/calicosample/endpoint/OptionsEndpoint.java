package calicosample.endpoint;

import java.util.List;

import javax.inject.Inject;

import calicosample.core.options.OptionsForm;
import calicosample.core.options.OptionsManager;
import calicosample.core.options.OptionsRecord;
import calicosample.endpoint.userinfo.UserInfoEndpoint;

public class OptionsEndpoint extends UserInfoEndpoint<List<OptionsForm>, List<OptionsRecord>> {
  @Inject OptionsManager optionsManager;

  @Override
  public List<OptionsRecord> execute(List<OptionsForm> forms) {
    return optionsManager.get(forms);
  }
}
