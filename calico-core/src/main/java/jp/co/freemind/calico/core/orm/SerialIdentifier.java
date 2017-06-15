package jp.co.freemind.calico.core.orm;


import javax.annotation.Nonnull;

public class SerialIdentifier extends Identifier<Integer> {
  public SerialIdentifier(@Nonnull Integer value) {
    super(value);
  }
}
