package jp.co.freemind.calico.core.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jp.co.freemind.calico.core.zone.Zone;

/**
 * Created by tasuku on 15/04/28.
 */
public class MediaProxy extends Media {
  private String id;

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

  private MediaMeta meta;
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

    MediaStorage storage = Zone.getCurrent().getInstance(MediaStorage.class);
    MediaProxy proxy = new MediaProxy(storage);
    proxy.setId(media.getId());
    proxy.setMeta(media.getMeta());
    proxy.instance = media;
    Zone.getCurrent().injectMembers(proxy);
    return proxy;
  }

  @JsonCreator
  private static MediaProxy create(
      @JsonProperty("id") String id,
      @JsonProperty("meta") MediaMeta meta) {
    MediaStorage storage = Zone.getCurrent().getInjector().getInstance(MediaStorage.class);
    MediaProxy proxy = new MediaProxy(storage);
    proxy.setId(id);
    proxy.setMeta(meta);
    return proxy;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMeta(MediaMeta meta) {
    this.meta = meta;
  }
}
