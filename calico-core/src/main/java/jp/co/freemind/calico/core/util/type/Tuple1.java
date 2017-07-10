package jp.co.freemind.calico.core.util.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class Tuple1<T1> implements Tuple {
  @Getter
  private final T1 value1;
}
