package jp.co.freemind.calico.config.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j2;

/**
 * Created by kakusuke on 15/05/25.
 */
@Log4j2
class PropertiesLoader {
  private final List<Function<String, InputStream>> strategies = Lists.newArrayList();

  PropertiesLoader() {
    strategies.add(PropertiesLoader::lookupResource);
  }

  void addLookupPath(Path basePath) {
    addLookupPath(basePath, false);
  }
  void addLookupPath(Path basePath, boolean secure) {
    strategies.add(getLookupPathStrategy(basePath, secure));
  }

  Properties load(String filename, boolean required) {
    Properties properties = new Properties();
    for (Function<String, InputStream> strategy : strategies) {
      try (InputStream is = strategy.apply(filename);
           InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8))  {
        properties.load(isr);
      }
      catch (Exception e) {}
    }
    if (required && properties.size() == 0) throw new IllegalArgumentException(filename + " の読み込みに失敗しました");
    return properties;
  }

  private static InputStream lookupResource(String path) {
    try {
      InputStream is = Resources.getResource(path).openStream();
      log.info("[プロパティ読み出し] <RESOURCE_ROOT>/" + path);
      return is;
    } catch (IOException e) {
      log.info("[プロパティ読み出し失敗] <RESOURCE_ROOT>/" + path);
      throw Throwables.propagate(e);
    }
  }

  private static InputStream lookupPath(Path path) {
    try {
      InputStream is = Files.newInputStream(path);
      log.info("[プロパティ読み出し] " + path);
      return is;
    } catch (IOException e) {
      log.info("[プロパティ読み出し失敗] " + path);
      throw Throwables.propagate(e);
    }
  }

  private Function<String, InputStream> getLookupPathStrategy(Path basePath, boolean secure) {
    return (String path) -> {
      Path propertiesPath = basePath.resolve(path);
      if (secure) validatePermission(basePath.resolve(path));
      return lookupPath(propertiesPath);
    };
  }

  private void validatePermission(Path path) {
     try {
       Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path);
       SecurityException exception = new SecurityException(path + " のパーミッションは0600にしてください");
       if (permissions.contains(PosixFilePermission.GROUP_READ)) throw exception;
       if (permissions.contains(PosixFilePermission.GROUP_WRITE)) throw exception;
       if (permissions.contains(PosixFilePermission.GROUP_EXECUTE)) throw exception;
       if (permissions.contains(PosixFilePermission.OTHERS_READ)) throw exception;
       if (permissions.contains(PosixFilePermission.OTHERS_WRITE)) throw exception;
       if (permissions.contains(PosixFilePermission.OTHERS_EXECUTE)) throw exception;
    }
    catch (UnsupportedOperationException | IOException e) { }
  }
}
