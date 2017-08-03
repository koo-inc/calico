package jp.co.freemind.calico.core.media;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data public class Media {
  @Nullable
  private String id;
  private MediaMeta meta = new MediaMeta();
  @JsonIgnore
  private byte[] payload = new byte[0];

  public static Media create(byte[] payload, String filename, String type) {
    MediaMeta meta = new MediaMeta();
    meta.setName(filename);
    meta.setType(type);
    meta.setSize((long) payload.length);
    Media media = new Media();
    media.setMeta(meta);
    media.setPayload(payload);
    return media;
  }
}
