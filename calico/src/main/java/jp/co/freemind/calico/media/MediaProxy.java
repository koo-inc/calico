package jp.co.freemind.calico.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tasuku on 15/04/28.
 */
public class MediaProxy extends Media {
  @Getter @Setter private String id;

  private MediaStorage storage;

  public MediaProxy(MediaStorage storage) {
    this.storage = storage;
  }

  @JsonIgnore
  @Override
  public byte[] getPayload() {
    return getInstance().getPayload();
  }

  @Override
  public void setPayload(byte[] content) {
    getInstance().setPayload(content);
  }

  @Setter private MediaMeta meta;
  @Override
  public MediaMeta getMeta() {
    if (meta != null) return meta;
    meta = storage.getMeta(id).orElseGet(MediaMeta::new);
    return meta;
  }

  private Media instance;
  private Media getInstance() {
    if (instance != null) return instance;
    instance = storage.get(id).orElseGet(Media::new);
    instance.setId(id);
    if (meta == null) {
      meta = instance.getMeta();
    }
    return instance;
  }

  @Override
  protected MediaProxy clone() {
    MediaProxy proxy = new MediaProxy(this.storage);
    proxy.id = this.id;
    proxy.meta = this.meta;
    proxy.instance = this.instance;
    return proxy;
  }

  public static MediaProxy of(Media media) {
    if (media instanceof MediaProxy) {
      return ((MediaProxy) media).clone();
    }

    MediaStorage storage = InjectUtil.getInstance(MediaStorage.class);
    MediaProxy proxy = new MediaProxy(storage);
    proxy.setId(media.getId());
    proxy.setMeta(media.getMeta());
    proxy.instance = media;
    InjectUtil.injectMembers(proxy);
    return proxy;
  }

  @JsonCreator
  private static MediaProxy create(
      @JsonProperty("id") String id,
      @JsonProperty("meta") MediaMeta meta) {
    MediaStorage storage = InjectUtil.getInjector().getInstance(MediaStorage.class);
    MediaProxy proxy = new MediaProxy(storage);
    proxy.setId(id);
    proxy.setMeta(meta);
    return proxy;
  }
}
