package calicosample.core.front.module;

import java.util.Arrays;
import java.util.List;

import jp.co.freemind.calico.jersey.FrontModule;
import jp.co.freemind.calico.jersey.frontmodule.AllPartials;

public class CalicoSample implements FrontModule, AllPartials {

  @Override
  public String getBasePath(){
    return "calico-sample";
  }
  @Override
  public List<String> getStyles() {
    return Arrays.asList(".");
  }
  @Override
  public List<String> getScripts() {
    return Arrays.asList("_init.js", ".");
  }

}
