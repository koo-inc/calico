package jp.co.freemind.calico.core.orm;

import javax.annotation.Nonnull;

public class BigSerialIdentifier extends Identifier<Long> {
  public BigSerialIdentifier(@Nonnull Long value) {
    super(value);
  }
}
