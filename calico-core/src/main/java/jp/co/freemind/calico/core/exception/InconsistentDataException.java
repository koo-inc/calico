package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.validation.Violation;

public class InconsistentDataException extends VerificationException {
  public InconsistentDataException(String message) {
    super(message);
  }

  public InconsistentDataException(String key, String message) {
    super(key, message);
  }

  public InconsistentDataException(Violation violation) {
    super(violation);
  }
}
