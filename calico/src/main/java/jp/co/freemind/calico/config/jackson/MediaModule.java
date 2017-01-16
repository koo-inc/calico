package jp.co.freemind.calico.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.co.freemind.calico.config.jackson.deser.MediaDeserializer;
import jp.co.freemind.calico.config.jackson.ser.MediaSerializer;
import jp.co.freemind.calico.media.Media;

/**
 * Created by tasuku on 15/05/01.
 */
public class MediaModule extends SimpleModule {
  public MediaModule() {
    addSerializer(Media.class, new MediaSerializer());
    addDeserializer(Media.class, new MediaDeserializer());
  }
}
