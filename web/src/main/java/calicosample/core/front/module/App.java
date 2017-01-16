package calicosample.core.front.module;

import calicosample.core.front.FrontResource;
import calicosample.core.util.ResourceUtil;
import jp.co.freemind.calico.jersey.FrontModule;
import jp.co.freemind.calico.jersey.frontmodule.AllPartials;
import jp.co.freemind.calico.jersey.frontmodule.AllScripts;
import jp.co.freemind.calico.jersey.frontmodule.AllStyles;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App implements FrontModule,
    AllScripts, AllStyles, AllPartials {

  private Path basePath;
  public App(Class<? extends FrontResource> resourceClass) {
    this.basePath = Paths.get("app").resolve(ResourceUtil.toAssetsContextPath(resourceClass));
  }

  @Override
  public String getBasePath() {
    return basePath.toString();
  }
}
