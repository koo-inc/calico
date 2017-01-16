package jp.co.freemind.calico.media;

import lombok.Data;

/**
 * Created by tasuku on 15/04/30.
 */
@Data public class MediaMeta {
  private String name = "unknown";
  private String type = "application/octet-stream";
  private Long size = 0L;
//  private long lastModified;
}
