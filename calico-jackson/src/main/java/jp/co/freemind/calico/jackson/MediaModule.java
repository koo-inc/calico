package jp.co.freemind.calico.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.co.freemind.calico.jackson.ser.MediaSerializer;
import jp.co.freemind.calico.jackson.deser.MediaDeserializer;
import jp.co.freemind.calico.core.media.Media;

/**
 * Created by tasuku on 15/05/01.
 */
public class MediaModule extends SimpleModule {
  public MediaModule() {
    addSerializer(Media.class, new MediaSerializer());
    addDeserializer(Media.class, new MediaDeserializer());
  }
}
