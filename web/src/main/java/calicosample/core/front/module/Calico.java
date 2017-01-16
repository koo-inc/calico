package calicosample.core.front.module;

import java.util.Arrays;
import java.util.List;

import jp.co.freemind.calico.jersey.FrontModule;
import jp.co.freemind.calico.jersey.frontmodule.AllStyles;

public class Calico implements FrontModule, AllStyles {

  @Override
  public String getBasePath() {
    return "calico";
  }
  @Override
  public List<String> getScripts() {
    return Arrays.asList("_init.js", "error.js", ".");
  }
}
