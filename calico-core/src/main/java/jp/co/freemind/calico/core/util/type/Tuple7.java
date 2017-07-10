package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple7<T1, T2, T3, T4, T5, T6, T7> extends Tuple6<T1, T2, T3, T4, T5, T6> {
  @Getter
  private final T7 value7;

  public Tuple7(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7) {
    super(value1, value2, value3, value4, value5, value6);
    this.value7 = value7;
  }
}