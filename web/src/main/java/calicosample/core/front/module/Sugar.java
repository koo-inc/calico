package calicosample.core.front.module;

import jp.co.freemind.calico.jersey.FrontModule;

import java.util.Arrays;
import java.util.List;

public class Sugar implements FrontModule {

  @Override
  public String getBasePath() {
    return "vendor/sugar-1.4.1";
  }

  @Override
  public List<String> getScripts() {
    return Arrays.asList("sugar.full.js");
  }
}
