package calicosample.endpoint.system;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import calicosample.endpoint.userinfo.UserInfoEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import lombok.Getter;
import lombok.Setter;

public class ExtEnumEndpoint extends UserInfoEndpoint<ExtEnumEndpoint.Input, ExtEnumEndpoint.Output> {
  private static Map<String, Object> CACHE = Maps.newHashMap();

  @Inject private ObjectMapper objectMapper;

  @Getter @Setter
  public static class Input {
    private List<String> keys;
  }

  @Getter @Setter
  public static class Output extends HashMap<String, Object> {}

  @Override
  @SuppressWarnings("unchecked")
  public Output execute(Input form) {
    Output output = new Output();
    form.getKeys()
      .forEach(key -> {
        Object data = CACHE.get(key);
        if(data == null){
          String className = "calicosample.extenum." + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, key);
          try {
            Class<? extends ExtEnum<?>> extEnumClass = (Class<? extends ExtEnum<?>>) Class.forName(className, false, this.getClass().getClassLoader());
            ExtEnum<?>[] values = (ExtEnum<?>[]) extEnumClass.getMethod("values").invoke(null);
            String json = objectMapper.writeValueAsString(values);
            data = objectMapper.readValue(json, Object.class);
            CACHE.put(key, data);
          } catch (ClassNotFoundException e) {
            throw new RuntimeException("unknown ExtEnum :" + key);
          } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("fail on get values method. :" + key);
          } catch (JsonProcessingException e) {
            throw new RuntimeException("fail on serialize values. :" + key);
          } catch (IOException e) {
            throw new RuntimeException("fail on deserialize values. :" + key);
          }
        }
        output.put(key, data);
      });
    return output;
  }
}
