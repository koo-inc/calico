package jp.co.freemind.calico.endpoint;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.google.common.base.CaseFormat;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.endpoint.exception.UnknownEndpointException;
import jp.co.freemind.calico.guice.InjectUtil;

public class EndpointManager {
  private Map<String, Optional<Class<? extends Endpoint>>> classMap = new ConcurrentHashMap<>();
  private Map<String, Optional<Type>> inputTypeMap = new ConcurrentHashMap<>();

  private static Pattern FRAGMENT = Pattern.compile("^[A-Za-z][_A-Za-z0-9]*");

  @SuppressWarnings("unchecked")
  private Class<? extends Endpoint> getEndpointClass(String path){
    return classMap.computeIfAbsent(normalizePath(path), p -> {
      try {
        String[] fragments = p.split("/+");
        if (fragments.length == 0) return Optional.empty();

        StringBuilder builder = new StringBuilder(Env.getRootPackage()).append(".endpoint.");
        for (int i = 0, len = fragments.length - 1; i < len; i++) {
          String fragment = fragments[i];
          if (!FRAGMENT.matcher(fragment).matches()) return Optional.empty();
          builder.append(fragment).append('.');
        }
        String fragment = fragments[fragments.length - 1];
        if (!FRAGMENT.matcher(fragment).matches()) return Optional.empty();
        builder.append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, fragment.toLowerCase())).append("Endpoint");

        Class<? extends Endpoint> endpointClass = (Class<? extends Endpoint>) Class.forName(builder.toString());
        if (Modifier.isAbstract(endpointClass.getModifiers())) return Optional.empty();
        if (Modifier.isInterface(endpointClass.getModifiers())) return Optional.empty();
        return Optional.of(endpointClass);
      } catch (ClassNotFoundException e) {
        return Optional.empty();
      }
    }).orElseThrow(() -> new UnknownEndpointException(path));
  }

  public Endpoint getEndpoint(String path){
    return InjectUtil.getInstance(getEndpointClass(path));
  }

  public Optional<Type> getInputType(String path){
    return inputTypeMap.computeIfAbsent(normalizePath(path), p ->
      Arrays.stream(getEndpointClass(p).getMethods())
        .filter(m ->  m.getName().equals("execute"))
        .filter(m -> !(m.getParameterTypes()[0] == Object.class && m.getReturnType() == Object.class)) //内部的に (Object) -> Object なexecuteが生成される場合がある
        .findFirst()
        .map(m -> m.getParameters()[0].getParameterizedType())
    );
  }

  private String normalizePath(String path) {
    return path.replaceAll("(^/+|/+$)", "").replaceAll("/+", "/");
  }
}
