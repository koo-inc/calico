package jp.co.freemind.calico.core.orm;

import javax.annotation.Nullable;

public class SerialIdentifier extends Identifier<Integer> {
  public SerialIdentifier(@Nullable Integer value) {
    super(value);
  }
}
