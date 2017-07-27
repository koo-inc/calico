package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.validation.Violation;

public class ApplicationException extends ViolationException {
  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String key, String message) {
    super(key, message);
  }

  public ApplicationException(Violation violation) {
    super(violation);
  }
}
