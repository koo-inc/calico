package jp.co.freemind.calico.core.media;

public class MediaMeta {
  private String name = "unknown";
  private String type = "application/octet-stream";
  private Long size = 0L;

  public MediaMeta() {
  }

  public String getName() {
    return this.name;
  }

  public String getType() {
    return this.type;
  }

  public Long getSize() {
    return this.size;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof MediaMeta)) return false;
    final MediaMeta other = (MediaMeta) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final Object this$type = this.getType();
    final Object other$type = other.getType();
    if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
    final Object this$size = this.getSize();
    final Object other$size = other.getSize();
    if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $type = this.getType();
    result = result * PRIME + ($type == null ? 43 : $type.hashCode());
    final Object $size = this.getSize();
    result = result * PRIME + ($size == null ? 43 : $size.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof MediaMeta;
  }

  public String toString() {
    return "MediaMeta(name=" + this.getName() + ", type=" + this.getType() + ", size=" + this.getSize() + ")";
  }
//  private long lastModified;
}
