package jp.co.freemind.calico.servlet.assets;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jp.co.freemind.calico.core.di.InjectorRef;

@Singleton
@SuppressWarnings("unchecked")
public class AssetsFinder {
  private final AssetsSetting setting;
  private LoadingCache<String, Optional<Asset>> cache;
  private final Object cacheLock = new Object[0];

  @Inject
  public AssetsFinder(AssetsSetting setting) {
    this.setting = setting;
  }

  public Optional<Asset> getAsset(@Nonnull ServletContext context, @Nullable String path) {
    if (path == null) return Optional.empty();
    String realPath =  context.getRealPath("/" + setting.getBaseDir()) + "/" + path;
    try {
      if (setting.cacheEnabled()) {
        return getCache().get(realPath);
      }
      else {
        return getAssetInstance(Paths.get(realPath));
      }
    } catch (ExecutionException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private LoadingCache<String, Optional<Asset>> getCache() {
    if (cache != null) return cache;
    synchronized (cacheLock) {
      if (cache != null) return cache;
      cache = CacheBuilder.newBuilder()
        .maximumSize(InjectorRef.getCurrent().getInstance(AssetsSetting.class).cacheEnabled() ? 5000 : 0)
        .build(new AssetCacheLoader());
      return cache;
    }
  }

  private Optional<Asset> getAssetInstance(Path path) {
    try (FileChannel channel = FileChannel.open(path)) {
      return Optional.of(new Asset(
        channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()),
        Files.probeContentType(path),
        channel.size(),
        Files.getLastModifiedTime(path).to(TimeUnit.SECONDS)
      ));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private class AssetCacheLoader extends CacheLoader<String, Optional<Asset>> {
    @Override
    public Optional<Asset> load(@Nonnull String path) throws Exception {
      return getAssetInstance(Paths.get(path));
    }
  }
}
