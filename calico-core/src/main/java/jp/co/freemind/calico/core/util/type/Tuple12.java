package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
  @Getter
  private final T12 value12;

  public Tuple12(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
    this.value12 = value12;
  }
}