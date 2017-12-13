package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.validation.Violation;

public class ApplicationException extends ViolationException {
  public ApplicationException(Message message, Object... args) {
    super(message, args);
  }
  public ApplicationException(String message) {
    super(message);
  }
  public ApplicationException(String key, String message) {
    super(key, message);
  }
  public ApplicationException(Violation violation) {
    super(violation);
  }
  public ApplicationException(String message, Throwable e) {
    super(message, e);
  }
  public ApplicationException(String key, String message, Throwable e) {
    super(key, message, e);
  }
  public ApplicationException(Violation violation, Throwable e) {
    super(violation, e);
  }
}
