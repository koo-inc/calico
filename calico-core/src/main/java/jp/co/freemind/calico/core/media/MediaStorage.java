package jp.co.freemind.calico.core.media;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by tasuku on 15/04/28.
 */
public interface MediaStorage {
  Optional<Media> get(String id);
  Optional<MediaMeta> getMeta(String id);
  MediaProxy store(Media media);
  MediaProxy store(Media media, Function<Media, String> idGenerator);
  void remove(Media media);
}
