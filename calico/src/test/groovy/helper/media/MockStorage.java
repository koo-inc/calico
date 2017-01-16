package helper.media;

import com.google.common.collect.Maps;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaMeta;
import jp.co.freemind.calico.media.MediaProxy;
import jp.co.freemind.calico.media.MediaStorage;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by kakusuke on 15/05/12.
 */
public class MockStorage implements MediaStorage {
  private static final Map<String, Media> storage = Maps.newHashMap();
  private static final Map<String, MediaMeta> metaStorage = Maps.newHashMap();

  @Override
  public Optional<Media> get(String id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<MediaMeta> getMeta(String id) {
    return Optional.ofNullable(metaStorage.get(id));
  }

  @Override
  public MediaProxy store(Media media) {
    return store(media, this::increment);
  }

  @Override
  public MediaProxy store(Media media, Function<Media, String> idGenerator) {
    if (media.getId() == null) {
      media.setId(idGenerator.apply(media));
    }
    storage.put(media.getId(), media);
    metaStorage.put(media.getId(), media.getMeta());
    return MediaProxy.of(media);
  }

  @Override
  public void remove(Media media) {
    storage.remove(media.getId());
    metaStorage.remove(media.getId());
  }

  private int i = 0;
  private String increment(Media media) {
    return "" + (i++);
  }
}
