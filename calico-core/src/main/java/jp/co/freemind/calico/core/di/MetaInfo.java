package jp.co.freemind.calico.core.di;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MetaInfo {

  private final Map<String, String> info;

  MetaInfo(Set<Map.Entry<String, String>> infoEntries) {
    this.info = Collections.unmodifiableMap(infoEntries.stream()
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  public int size() { return info.size(); }
  public boolean containsKey(String key) { return info.containsKey(key); }
  public boolean containsValue(String value) { return info.containsValue(value); }
  public String get(String key) { return info.get(key); }
  public Set<String> keySet() { return info.keySet(); }
  public Collection<String> values() { return info.values(); }
  public Set<Map.Entry<String, String>> entrySet() { return info.entrySet(); }
  public String getOrDefault(String key, String defaultValue) { return info.getOrDefault(key, defaultValue); }
  public void forEach(BiConsumer<? super String, ? super String> action) { info.forEach(action); }

  Map<String, String> toMutableMap() {
    Map<String, String> map = new HashMap<>();
    forEach(map::put);
    return map;
  }
}
