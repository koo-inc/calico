package jp.co.freemind.calico.core.extenum;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface ExtEnum<T> {

  T getId();

  @JsonProperty("name")
  String getName();

  @JsonIgnore
  default T getValue() { return getId(); }

  @JsonProperty("NAME")
  String name();

  @Deprecated
  @SuppressWarnings("unchecked")
  static <T, E extends ExtEnum<T>> E of(Object id, E... e) {
    if(id instanceof Map){
      id = ((Map)id).get("id");
    }
    return of((T) id, (Class<E>) e.getClass().getComponentType());
  }

  static <T, E extends ExtEnum<T>> E of(T id, Class<E> enumClass) {
    for (E ee : values(enumClass)) {
      if (String.valueOf(ee.getId()).equals(String.valueOf(id))) {
        return ee;
      }
    }
    return null;
  }

  static <T, E extends ExtEnum<T>> E[] values(Class<E> enumClass) {
    return enumClass.getEnumConstants();
  }
}
