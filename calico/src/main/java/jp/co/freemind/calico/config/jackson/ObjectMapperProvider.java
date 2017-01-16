package jp.co.freemind.calico.config.jackson;

import java.util.Arrays;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jp.co.freemind.calico.json.JsonObjectModule;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
  final ObjectMapper mapper;

  public ObjectMapperProvider() {
    mapper = createMapper();
  }

  public static ObjectMapper createMapper() {
    ObjectMapper ret = new ObjectMapper();

    // Optional 対応
    ret.registerModule(new Jdk8Module());
    // LocalDate 等対応
    ret.registerModule(new JavaTimeModule());
    ret.registerModule(new CalicoJavaTimeModulePatch());

    ret.registerModule(new MediaModule());

    // JsonObject 対応
    ret.registerModule(new JsonObjectModule());

    return ret;
  }

  public static ObjectMapper createMapper(Module... modules) {
    ObjectMapper mapper = createMapper();
    Arrays.stream(modules).forEach(mapper::registerModule);
    return mapper;
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return mapper;
  }
}
