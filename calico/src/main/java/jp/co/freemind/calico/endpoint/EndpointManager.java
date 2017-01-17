package jp.co.freemind.calico.endpoint;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CaseFormat;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.endpoint.exception.UnknownEndpointException;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.util.ClassFinder;

public class EndpointManager {
  private Map<String, Class<? extends Endpoint>> classMap = new HashMap<>();

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

  public Endpoint getEndpoint(String path){
    return InjectUtil.getInstance(getEndpointClass(path));
  }

  private Class<? extends Endpoint> getEndpointClass(String path){
    if(!classMap.containsKey(path)){
      throw new UnknownEndpointException(path);
    }
    return classMap.get(path);
  }
}
