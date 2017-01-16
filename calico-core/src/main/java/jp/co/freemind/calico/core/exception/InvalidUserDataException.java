package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.validation.Violation;

public class InvalidUserDataException extends VerificationException {
  public InvalidUserDataException(String message) {
    super(message);
  }
  public InvalidUserDataException(String key, String message) {
    super(key, message);
  }
  public InvalidUserDataException(Violation violation) {
    super(violation);
  }
}
