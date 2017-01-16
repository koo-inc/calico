package jp.co.freemind.calico.servlet.assets;

import java.nio.ByteBuffer;


public class Asset {
  private final ByteBuffer content;
  private final String contentType;
  private final long contentLength;
  private final long lastModified;

  @java.beans.ConstructorProperties({"content", "contentType", "contentLength", "lastModified"})
  public Asset(ByteBuffer content, String contentType, long contentLength, long lastModified) {
    this.content = content;
    this.contentType = contentType;
    this.contentLength = contentLength;
    this.lastModified = lastModified;
  }

  public ByteBuffer getContent() {
    return this.content;
  }

  public String getContentType() {
    return this.contentType;
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public long getLastModified() {
    return this.lastModified;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Asset)) return false;
    final Asset other = (Asset) o;
    final Object this$content = this.getContent();
    final Object other$content = other.getContent();
    if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
    final Object this$contentType = this.getContentType();
    final Object other$contentType = other.getContentType();
    if (this$contentType == null ? other$contentType != null : !this$contentType.equals(other$contentType))
      return false;
    if (this.getContentLength() != other.getContentLength()) return false;
    if (this.getLastModified() != other.getLastModified()) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $content = this.getContent();
    result = result * PRIME + ($content == null ? 43 : $content.hashCode());
    final Object $contentType = this.getContentType();
    result = result * PRIME + ($contentType == null ? 43 : $contentType.hashCode());
    final long $contentLength = this.getContentLength();
    result = result * PRIME + (int) ($contentLength >>> 32 ^ $contentLength);
    final long $lastModified = this.getLastModified();
    result = result * PRIME + (int) ($lastModified >>> 32 ^ $lastModified);
    return result;
  }

  public String toString() {
    return "jp.co.freemind.calico.servlet.assets.Asset(content=" + this.getContent() + ", contentType=" + this.getContentType() + ", contentLength=" + this.getContentLength() + ", lastModified=" + this.getLastModified() + ")";
  }
}
