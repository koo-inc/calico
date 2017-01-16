package calicosample.core.fmstorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaMeta;
import jp.co.freemind.calico.media.MediaProxy;
import jp.co.freemind.calico.media.MediaStorage;
import lombok.extern.log4j.Log4j2;

/**
 * Created by tasuku on 15/04/27.
 */
@Log4j2
public class FmStorage implements MediaStorage {
  @Override
  public Optional<Media> get(String id) {
    Optional<MediaMeta> meta = getMeta(id);
    return meta.map(m -> {
      Map<String, Object> param = Maps.newHashMap();
      param.put("path", FmStorageEnv.getEnv().getBasePath() + id);
      try (InputStream is = FmStorageCommand.GET.execute(param)) {
        Media media = new Media();
        media.setId(id);
        media.setMeta(m);
        media.setPayload(ByteStreams.toByteArray(is));
        return media;
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
    });
  }

  @Override
  public Optional<MediaMeta> getMeta(String id) {
    Map<String, Object> param = Maps.newHashMap();
    try {
      param.put("path", FmStorageEnv.getEnv().getBasePath() + id);
      return Optional.of(FmStorageCommand.META.execute(param));
    }
    catch(Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public MediaProxy store(Media media) {
    return store(media, MediaIdGenerator::hashing);
  }

  @Override
  public MediaProxy store(Media media, Function<Media, String> idGenerator) {
    MediaProxy proxy = MediaProxy.of(media);
    if (proxy.getId() == null) {
      proxy.setId(idGenerator.apply(media));
    }
    Map<String, Object> param = Maps.newHashMap();
    param.put("path", FmStorageEnv.getEnv().getBasePath() + proxy.getId());
    param.put("name", proxy.getMeta().getName());
    param.put("type", proxy.getMeta().getType());
    param.put("data", proxy.getPayload());
    FmStorageCommand.PUT.execute(param);
    return proxy;
  }

  @Override
  public void remove(Media media) {
    try {
      Map<String, Object> param = Maps.newHashMap();
      param.put("path", FmStorageEnv.getEnv().getBasePath() + media.getId());
      FmStorageCommand.DELETE.execute(param);
    }
    catch (RuntimeException e) {
      log.info(Throwables.getStackTraceAsString(e));
    }
  }

  /*
  public void copy(String id, String dest) {
    Map<String, Object> param = Maps.newHashMap();
    param.put("path", FmStorageEnv.getEnv().getBasePath() + id);
    param.put("dest", dest);
    FmStorageCommand.COPY.execute(param);
  }
  */

  /*
  public List<String> getIdList(String idPartial) {
    Map<String, Object> param = Maps.newHashMap();
    String basePath = FmStorageEnv.getEnv().getBasePath();
    param.put("path", basePath + idPartial);
    List<String> list = FmStorageCommand.LIST.execute(param);
    return list.stream().map(id -> id.substring(basePath.length())).collect(Collectors.toList());
  }
  */
}
