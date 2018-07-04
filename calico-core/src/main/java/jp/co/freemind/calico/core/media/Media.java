package jp.co.freemind.calico.core.media;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Media {
  @Nullable
  private String id;
  private MediaMeta meta = new MediaMeta();
  @JsonIgnore
  private byte[] payload = new byte[0];

  public Media() {
  }

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

  @Nullable
  public String getId() {
    return this.id;
  }

  public MediaMeta getMeta() {
    return this.meta;
  }

  public byte[] getPayload() {
    return this.payload;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public void setMeta(MediaMeta meta) {
    this.meta = meta;
  }

  public void setPayload(byte[] payload) {
    this.payload = payload;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Media)) return false;
    final Media other = (Media) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final Object this$meta = this.getMeta();
    final Object other$meta = other.getMeta();
    if (this$meta == null ? other$meta != null : !this$meta.equals(other$meta)) return false;
    if (!java.util.Arrays.equals(this.getPayload(), other.getPayload())) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $meta = this.getMeta();
    result = result * PRIME + ($meta == null ? 43 : $meta.hashCode());
    result = result * PRIME + java.util.Arrays.hashCode(this.getPayload());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Media;
  }

  public String toString() {
    return "Media(id=" + this.getId() + ", meta=" + this.getMeta() + ", payload=" + java.util.Arrays.toString(this.getPayload()) + ")";
  }
}
