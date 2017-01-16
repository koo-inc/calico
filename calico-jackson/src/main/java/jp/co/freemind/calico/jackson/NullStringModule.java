package jp.co.freemind.calico.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.co.freemind.calico.jackson.deser.NullStringDeserializer;

public class NullStringModule extends SimpleModule {
  public NullStringModule() {
    addDeserializer(String.class, new NullStringDeserializer());
  }
}
