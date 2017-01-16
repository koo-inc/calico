package calicosample.core.front.module;

import java.util.Arrays;
import java.util.List;

import jp.co.freemind.calico.jersey.FrontModule;

public class ApplicationInit implements FrontModule {

  @Override
  public String getBasePath() {
    return "app";
  }
  @Override
  public List<String> getScripts() {
    return Arrays.asList("_init.js");
  }
}
