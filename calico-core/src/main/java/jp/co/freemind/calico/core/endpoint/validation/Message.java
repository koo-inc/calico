package jp.co.freemind.calico.core.endpoint.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Message {
  private static final Pattern VARIABLE = Pattern.compile("(\\{[^}]+})");

  private final String template;
  private final Function<FieldAccessor, Map<String, Object>> mapper;
  public Message(String template, Function<FieldAccessor, Map<String, Object>> messageArgumentsMapper) {
    this.template = template;
    this.mapper = messageArgumentsMapper;
  }
  public Message(String message) {
    this(message, field -> null);
  }

  public String value(Object... args) {
    Map<String, Object> argMap = new HashMap<>();

    java.util.regex.Matcher matcher = VARIABLE.matcher(template);
    for (Object arg : args) {
      if (!matcher.find()) break;
      String str = matcher.group(1);
      argMap.put(str.substring(1, str.length() - 1).trim(), arg);
    }

    return value(argMap);
  }

  public String value(FieldAccessor field) {
    Map<String, Object> argMap = mapper.apply(field);
    if (argMap == null) return template;

    return value(argMap);
  }

  private String value(Map<String, Object> argMap) {

    String result = template;
    for (Map.Entry<String, Object> e : argMap.entrySet()) {
      result = result.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
    }
    return result;
  }

}
