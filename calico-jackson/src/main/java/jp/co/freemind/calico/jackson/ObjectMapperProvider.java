package jp.co.freemind.calico.jackson;

import java.util.Arrays;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ObjectMapperProvider implements Provider<ObjectMapper> {
  private static final ObjectMapper mapper = createMapper();

  @Override
  public ObjectMapper get() {
    return mapper;
  }

  public static ObjectMapper createMapper(Module... modules) {
    ObjectMapper mapper = ObjectMapperProvider.mapper.copy();
    Arrays.stream(modules).forEach(mapper::registerModule);
    return mapper;
  }

  private static ObjectMapper createMapper() {
    ObjectMapper ret = new ObjectMapper();

    // Optional 対応
    ret.registerModule(new Jdk8Module());
    // LocalDate 等対応
    ret.registerModule(new JavaTimeModule());
    ret.registerModule(new CalicoJavaTimeModulePatch());

    ret.registerModule(new MediaModule());

    // JsonObject 対応
    ret.registerModule(new JsonObjectModule());

    ret.registerModule(new IdentifierModule());

    ret.registerModule(new NullStringModule());

    return ret;
  }
}
