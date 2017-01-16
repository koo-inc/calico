package calicosample.core.front.module;

import jp.co.freemind.calico.jersey.FrontModule;

import java.util.Arrays;
import java.util.List;

public class JQuery implements FrontModule {
  @Override
  public String getBasePath() {
    return "vendor/jquery-1.11.2";
  }
  @Override
  public List<String> getScripts() {
    return Arrays.asList("jquery.js");
  }
}
