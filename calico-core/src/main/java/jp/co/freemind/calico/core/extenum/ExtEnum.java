package jp.co.freemind.calico.core.extenum;

import java.util.Map;
import java.util.Objects;

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

  @SuppressWarnings("unchecked")
  static <T, E extends ExtEnum<T>> E of(Object id, E... e) {
    return of(id, (Class<E>) e.getClass().getComponentType());
  }

  static <T, E extends ExtEnum<T>> E of(Object id, Class<E> enumClass) {
    if(id instanceof Map){
      id = ((Map)id).get("id");
    }
    for (E ee : values(enumClass)) {
      if (Objects.equals(ee.getId(), id)) {
        return ee;
      }
    }
    return null;
  }

  static <T, E extends ExtEnum<T>> E[] values(Class<E> enumClass) {
    return enumClass.getEnumConstants();
  }
}
