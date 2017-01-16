package jp.co.freemind.calico.jersey.assets;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Singleton;

import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

@Singleton
public class AssetsFinder {
  public static final String ASSETS_PATH = "assets/";

  public static final AssetsEnv assetsEnv = Env.loadPartial(AssetsEnv.class);

  public static final LoadingCache<Key, List<String>> CACHE = CacheBuilder.newBuilder()
      .maximumSize(assetsEnv.cacheEnabled() ? 5000 : 0)
      .build(new AssetCacheLoader());

  public List<String> findAssets(List<String> paths, String ext, int maxDepth) {
    return findAssets(paths.toArray(new String[paths.size()]), ext, maxDepth);
  }
  public List<String> findAssets(Stream<String> paths, String ext, int maxDepth) {
    return findAssets(paths.toArray(String[]::new), ext, maxDepth);
  }

  @SneakyThrows
  private List<String> findAssets(String[] paths, String ext, int maxDepth) {
    return CACHE.get(new Key(paths, ext, maxDepth));
  }

  public static List<String> find(String basePath, List<String> paths, String ext, int maxDepth) {
    Stream<String> pathStream = paths.stream().map(p -> p.replaceFirst("(^[!]|^)", "$1" + Matcher.quoteReplacement(basePath) + "/"));
    return InjectUtil.getInjector().getInstance(AssetsFinder.class)
      .findAssets(pathStream, ext, maxDepth);
  }

  @Value private static class Key {
    final String[] paths;
    final String ext;
    final int maxDepth;
  }

  @Log4j2
  private static class AssetCacheLoader extends CacheLoader<Key, List<String>> {
    @Override
    public List<String> load(Key key) throws Exception {
      log.trace("load key: " + key.toString());
      ServletContext servletContext = InjectUtil.getInstance(ServletContext.class);
      String root = servletContext.getRealPath("/" + ASSETS_PATH);

      Stream<Path> paths = Stream.of(key.getPaths()).map((p) -> Paths.get(root, p).normalize());

      return find(paths, key.getExt(), key.getMaxDepth())
        .map((p)-> Paths.get(root).getParent().relativize(p))
        .map(Path::toString)
        .collect(Collectors.toList());
    }

    private Stream<Path> find(Stream<Path> paths, String ext, int maxDepth) {
      return paths
        .flatMap((p) -> {
          try (Stream<Path> children = Files.find(p, maxDepth, (file, attrs) -> file.toString().endsWith("." + ext))) {
            return children.collect(Collectors.toList())
              .stream()
              .sorted(Comparator.comparing(Path::toString));
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        });
    }
  }
}
