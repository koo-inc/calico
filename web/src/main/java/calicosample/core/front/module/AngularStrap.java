package calicosample.core.front.module;

import java.util.Arrays;
import java.util.List;

import jp.co.freemind.calico.jersey.FrontModule;

public class AngularStrap implements FrontModule {

  @Override
  public String getBasePath() {
    return "vendor/angular-strap-2.3.1";
  }

  @Override
  public List<String> getScripts() {
    return Arrays.asList(
        "dist/angular-strap.js",
        "dist/angular-strap.tpl.js"
      );
  }
}
