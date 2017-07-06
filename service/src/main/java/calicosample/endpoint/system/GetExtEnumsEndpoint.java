package calicosample.endpoint.system;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import calicosample.endpoint.userinfo.UserInfoEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import jp.co.freemind.calico.core.util.ClassFinder;
import lombok.SneakyThrows;

public class GetExtEnumsEndpoint extends UserInfoEndpoint<EmptyInput, Map<String, Object>> {
  private static Map<String, Object> CACHE;

  @Inject private ObjectMapper objectMapper;

  @Override
  public Map<String, Object> execute(EmptyInput form) {
    if(CACHE == null){
      CACHE = createResult();
    }
    return CACHE;
  }

  @SuppressWarnings("unchecked")
  private List<Class<? extends ExtEnum<?>>> findExtEnumClasses(){
    return ClassFinder.findClasses("calicosample.extenum").stream()
      .filter(ExtEnum.class::isAssignableFrom)
      .map(c -> (Class<? extends ExtEnum<?>>)c)
      .collect(Collectors.toList());
  }
  private Map<String, Object> createResult(){
    return findExtEnumClasses().stream()
      .collect(Collectors.toMap(
        this::createResultKey,
        this::createResultValue
      ));
  }
  private String createResultKey(Class<? extends ExtEnum> enumClass){
    return enumClass.getSimpleName();
  }
  @SneakyThrows
  private Object createResultValue(Class<? extends ExtEnum<?>> enumClass){
    Method valuesMethod = enumClass.getMethod("values");
    ExtEnum<?>[] values = (ExtEnum<?>[]) valuesMethod.invoke(null);
    String json = objectMapper.writeValueAsString(values);
    return objectMapper.readValue(json, Object.class);
  }
}
