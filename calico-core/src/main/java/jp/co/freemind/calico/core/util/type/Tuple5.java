package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple5<T1, T2, T3, T4, T5> extends Tuple4<T1, T2, T3, T4> {
  @Getter
  private final T5 value5;

  public Tuple5(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
    super(value1, value2, value3, value4);
    this.value5 = value5;
  }
}
