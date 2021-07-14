package jp.co.freemind.calico.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class Value<T> {
  protected final Object rawValue;

  /**
   * Constructor
   */
  protected Value(@Nonnull Object rawValue) {
    this.rawValue = rawValue;
  }

  /**
   * getValue
   */
  @JsonValue
  public abstract T getValue();

  /**
   * toString
   */
  @Override
  public String toString() {
    return rawValue.toString();
  }

  /**
   * Utility
   */
  protected static boolean isBlank(Object rawValue){
    if(rawValue == null) return true;
    if(!(rawValue instanceof String)) return false;
    String value = (String) rawValue;
    int strLen = value.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(value.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  protected static Object trim(Object rawValue){
    if(!(rawValue instanceof String)) return rawValue;
    return ((String)rawValue).trim();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Value<?> value = (Value<?>) o;
    return Objects.equals(rawValue, value.rawValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rawValue);
  }
}
