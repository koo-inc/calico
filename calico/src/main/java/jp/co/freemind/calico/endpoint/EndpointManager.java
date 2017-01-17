package jp.co.freemind.calico.endpoint;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CaseFormat;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.endpoint.exception.UnknownEndpointException;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.util.ClassFinder;

public class EndpointManager {
  private Map<String, Class<? extends Endpoint>> classMap = new HashMap<>();
  private Map<String, Type> inputTypeMap = new HashMap<>();

  @SuppressWarnings("unchecked")
  public EndpointManager(){
    String rootPackage = Env.getRootPackage() + ".endpoint";
    ClassFinder.findClasses(rootPackage).stream()
      .filter(Endpoint.class::isAssignableFrom)
      .forEach(c -> {
        String path = c.getPackage().getName().substring(rootPackage.length()).replaceAll("\\.", "/");
        if(path.startsWith("/")) path = path.substring(1);
        if(path.length() > 0) path += "/";
        path += CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, c.getSimpleName().replaceAll("Endpoint$", ""));

        classMap.put(path, (Class<? extends Endpoint>)c);
      });
  }

  private Class<? extends Endpoint> getEndpointClass(String path){
    if(!classMap.containsKey(path)){
      throw new UnknownEndpointException(path);
    }
    return classMap.get(path);
  }

  public Endpoint getEndpoint(String path){
    return InjectUtil.getInstance(getEndpointClass(path));
  }

  public Type getInputType(String path){
    if(!inputTypeMap.containsKey(path)){
      Method executeMethod = Arrays.stream(getEndpointClass(path).getMethods())
        .filter(m ->  m.getName().equals("execute"))
        .filter(m -> !(m.getParameterTypes()[0] == Object.class && m.getReturnType() == Object.class)) //内部的に (Object) -> Object なexecuteが生成される場合がある
        .findFirst().orElseGet(null);
      inputTypeMap.put(path, executeMethod.getParameters()[0].getParameterizedType());
    }
    return inputTypeMap.get(path);
  }
}
