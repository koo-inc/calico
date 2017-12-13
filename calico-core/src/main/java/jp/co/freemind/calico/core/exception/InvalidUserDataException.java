package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.validation.Violation;

public class InvalidUserDataException extends ViolationException {
  public InvalidUserDataException(Message message, Object... args) {
    super(message, args);
  }
  public InvalidUserDataException(String message) {
    super(message);
  }
  public InvalidUserDataException(String key, String message) {
    super(key, message);
  }
  public InvalidUserDataException(Violation violation) {
    super(violation);
  }
  public InvalidUserDataException(String message, Throwable e) {
    super(message, e);
  }
  public InvalidUserDataException(String key, String message, Throwable e) {
    super(key, message, e);
  }
  public InvalidUserDataException(Violation violation, Throwable e) {
    super(violation, e);
  }
}
