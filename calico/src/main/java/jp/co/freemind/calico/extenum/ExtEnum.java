package jp.co.freemind.calico.extenum;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ExtEnum<T> {

  @JsonValue
  T getId();

  String getName();

  default T getValue() { return getId(); }

  @SuppressWarnings("unchecked")
  static <T, E extends ExtEnum<T>> E of(T id, E... e) {
    return of(id, (Class<E>) e.getClass().getComponentType());
  }

  static <T, E extends ExtEnum<T>> E of(T id, Class<E> enumClass) {
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
