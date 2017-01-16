package jp.co.freemind.calico.jersey;


import static com.google.common.base.CaseFormat.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.glassfish.jersey.server.mvc.Viewable;

import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.jersey.assets.AssetsFinder;
import jp.co.freemind.calico.util.StreamUtil;


public class MapViewable extends Viewable {

  private Map<String, Object> modelMap;

  private static final String STYLE_KEY = "_styles";
  private static final String SCRIPT_KEY = "_scripts";
  private static final String PARTIAL_KEY = "_partials";

  public MapViewable(String templateName) {
    this(templateName, new HashMap<>());
  }

  private MapViewable(String templateName, Map<String, Object> modelMap) {
    super(templateName, modelMap);
    InjectUtil.injectMembers(this);
    this.modelMap = modelMap;
    this.modelMap.computeIfAbsent(STYLE_KEY, (k) -> new LinkedHashSet<>());
    this.modelMap.computeIfAbsent(SCRIPT_KEY, (k) -> new LinkedHashSet<>());
    this.modelMap.computeIfAbsent(PARTIAL_KEY, (k) -> new LinkedHashMap<>());
  }

  public MapViewable add(String key, Object value) {
    modelMap.put(key, value);
    return this;
  }

  public MapViewable script(String... paths) {
    return script(Arrays.asList(paths));
  }
  @SuppressWarnings("unchecked")
  private MapViewable script(List<String> paths) {
    Set<String> scripts = (Set<String>) modelMap.get(SCRIPT_KEY);
    scripts.addAll(paths);
    return this;
  }

  public MapViewable style(String... paths) {
    return style(Arrays.asList(paths));
  }
  @SuppressWarnings("unchecked")
  private MapViewable style(List<String> paths) {
    Set<String> styles = (Set<String>) modelMap.get(STYLE_KEY);
    styles.addAll(paths);
    return this;
  }

  public MapViewable module(FrontModule... modules) {
    List<String> scripts = Stream.of(modules)
      .flatMap(m -> m.getScriptAssets().stream())
      .collect(Collectors.toList());
    script(scripts);

    List<String> styles = Stream.of(modules)
      .flatMap(m -> m.getStyleAssets().stream())
      .collect(Collectors.toList());
    style(styles);


    @SuppressWarnings("unchecked")
    Map<String, Map<String, String>> partialMap = (Map<String, Map<String, String>>) modelMap.get(PARTIAL_KEY);
    Stream.of(modules).forEach(m -> {
      String key = UPPER_CAMEL.to(UPPER_UNDERSCORE, m.getModuleName());
      partialMap.computeIfAbsent(key, k -> new HashMap<>());
      List<String> partials = m.getPartialAssets();
      StreamUtil.zip(
        partials.stream()
          // assets/hoge/fuga/piyo.html -> FUGA_PIYO
          .map(p -> p.substring(AssetsFinder.ASSETS_PATH.length() + m.getBasePath().length() + 1, p.length() - 5))
          .map(p -> p.replaceAll("[^0-9a-zA-Z]", "_"))
          .map(p -> LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, p))
          .collect(Collectors.toList()),
        partials)
        .forEach(p -> partialMap.get(key).put(p.getLeft(), p.getRight()));
    });
    return this;
  }
}
