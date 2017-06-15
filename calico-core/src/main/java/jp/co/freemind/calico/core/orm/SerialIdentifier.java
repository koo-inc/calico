package jp.co.freemind.calico.core.orm;

import com.sun.istack.internal.NotNull;

public class SerialIdentifier extends Identifier<Integer> {
  public SerialIdentifier(@NotNull Integer value) {
    super(value);
  }
}
