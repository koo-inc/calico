package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
  @Getter
  private final T4 value4;

  public Tuple4(T1 value1, T2 value2, T3 value3, T4 value4) {
    super(value1, value2, value3);
    this.value4 = value4;
  }
}
