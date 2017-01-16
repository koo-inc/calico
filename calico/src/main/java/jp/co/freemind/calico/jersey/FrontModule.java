package jp.co.freemind.calico.jersey;

import jp.co.freemind.calico.jersey.assets.AssetsFinder;

import java.util.Collections;
import java.util.List;

public interface FrontModule {
  String getBasePath();

  default List<String> getScripts() {
    return Collections.emptyList();
  }
  default List<String> getStyles() {
    return Collections.emptyList();
  }
  default List<String> getPartials() {
    return Collections.emptyList();
  }

  default List<String> getScriptAssets() {
    return AssetsFinder.find(getBasePath(), getScripts(), "js", getSearchDepth());
  }
  default List<String> getStyleAssets() {
    return AssetsFinder.find(getBasePath(), getStyles(), "css", getSearchDepth());
  }
  default List<String> getPartialAssets() {
    return AssetsFinder.find(getBasePath(), getPartials(), "html", getSearchDepth());
  }

  default int getSearchDepth() {
    return 10;
  }

  default String getModuleName() {
    return getClass().getSimpleName();
  }
}
