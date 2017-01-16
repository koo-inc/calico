package calicosample.core.front.module;

import java.util.Arrays;
import java.util.List;

import jp.co.freemind.calico.jersey.FrontModule;

public class Angular implements FrontModule {

  @Override
  public String getBasePath() {
    return "vendor/angular-1.4.3";
  }

  @Override
  public List<String> getScripts() {
    return Arrays.asList(
          "angular.js",
          "i18n/angular-locale_ja-jp.js",
          "angular-route.js",
          "angular-sanitize.js",
          "angular-animate.js"
        );
  }
}
