package calicosample.core.options.supplier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import calicosample.core.options.OptionsForm;
import calicosample.core.options.OptionsRecord;
import calicosample.core.options.OptionsSupplier;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import jp.co.freemind.calico.extenum.ExtEnum;
import jp.co.freemind.calico.util.ClassFinder;

public class ExtEnumOptionsSupplier implements OptionsSupplier {
  private static Map<String, ExtEnum<?>[]> CACHE = buildCache();

  @Override
  public boolean isKeyMatch(String key) {
    return true;
  }

  @Override
  public Optional<OptionsRecord> get(OptionsForm form) {
    List<Object> data = getData(form.getKey());
    return data == null ? Optional.empty() : Optional.of(OptionsRecord.cacheable(form.getKey(), Optional.of(data)));
  }

  private List<Object> getData(String key) {
    ExtEnum<?>[] values = CACHE.get(key);
    if (values == null) return null;
    return Stream.of(values)
      .map(ExtEnumOptionsSupplier::extEnumToMap)
      .collect(Collectors.toList());
  }
  private static <T extends ExtEnum<?>> LinkedHashMap<String, Object> extEnumToMap(T e) {
    return new LinkedHashMap<String, Object>() {{
      put("NAME", e.toString());
      for (Method method : e.getClass().getDeclaredMethods()) {
        if (!method.getName().startsWith("get") || method.getParameterCount() != 0) {
          continue;
        }
        try {
          String name = StringUtils.uncapitalize(method.getName().replaceFirst("^get", ""));
          Object value = method.invoke(e);
          put(name, value);
        } catch (IllegalAccessException | InvocationTargetException ignored) {}
      }
    }};
  }

  private static final String BASE_PACKAGE = "calicosample.extenum";
  private static Map<String, ExtEnum<?>[]> buildCache() {
    ImmutableMap.Builder<String, ExtEnum<?>[]> builder = ImmutableMap.builder();
    ClassFinder.findClasses(BASE_PACKAGE).stream().filter(ExtEnum.class::isAssignableFrom).forEach(c -> {
      String key = StringUtils.uncapitalize(c.getCanonicalName().substring(BASE_PACKAGE.length() + 1));
      try {
        builder.put(key, (ExtEnum<?>[]) c.getMethod("values").invoke(null));
      } catch (ReflectiveOperationException e) {
      }
    });
    return builder.build();
  }
}
