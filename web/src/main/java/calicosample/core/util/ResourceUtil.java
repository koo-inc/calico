package calicosample.core.util;

import calicosample.core.front.AssetsPath;
import calicosample.core.front.FrontResource;
import com.google.common.base.Strings;
import jp.co.freemind.calico.config.env.Env;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * Created by tasuku on 15/02/26.
 */
public class ResourceUtil {
  public static Path toAssetsContextPath(Class<? extends FrontResource> resourceClass) {
    AssetsPath assetsPath = resourceClass.getAnnotation(AssetsPath.class);
    if (assetsPath != null) {
      return Paths.get(assetsPath.value());
    }

    String frontResourcePackageName = Env.getFrontResourcePackage();
    String packageName = resourceClass.getPackage().getName();
    if (!packageName.startsWith(frontResourcePackageName)) {
      throw new IllegalStateException("リソースのパッケージ名の設定が違います");
    }

    String className = resourceClass.getSimpleName();

    String basePath = packageName.substring(frontResourcePackageName.length()).replaceFirst("^[.]", "").replaceAll("[.]", "/");
    String actionName = UPPER_CAMEL.to(LOWER_UNDERSCORE, className);

    if (Strings.isNullOrEmpty(basePath)) {
      return Paths.get(actionName);
    }

    return Paths.get(basePath, actionName);
  }
}
