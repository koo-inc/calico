package jp.co.freemind.calico.core.orm;

import javax.annotation.Nullable;

public class BigSerialIdentifier extends Identifier<Long> {
  public BigSerialIdentifier(@Nullable Long value) {
    super(value);
  }
}
