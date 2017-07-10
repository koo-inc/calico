package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
  @Getter
  private final T3 value3;

  public Tuple3(T1 value1, T2 value2, T3 value3) {
    super(value1, value2);
    this.value3 = value3;
  }
}
