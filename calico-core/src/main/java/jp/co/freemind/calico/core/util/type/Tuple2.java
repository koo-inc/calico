package jp.co.freemind.calico.core.util.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Tuple2<T1, T2> extends Tuple1<T1> {
  @Getter
  private final T2 value2;

  public Tuple2(T1 value1, T2 value2) {
    super(value1);
    this.value2 = value2;
  }
}
