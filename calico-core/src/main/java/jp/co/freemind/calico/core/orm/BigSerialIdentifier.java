package jp.co.freemind.calico.core.orm;

import com.sun.istack.internal.NotNull;

public class BigSerialIdentifier extends Identifier<Long> {
  public BigSerialIdentifier(@NotNull Long value) {
    super(value);
  }
}
