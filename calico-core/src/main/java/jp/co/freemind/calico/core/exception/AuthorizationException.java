package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.validation.Violation;

public class AuthorizationException extends ViolationException {
  public AuthorizationException(Message message, Object... args) {
    super(message, args);
  }
  public AuthorizationException(String message) {
    super(message);
  }
  public AuthorizationException(String key, String message) {
    super(key, message);
  }
  public AuthorizationException(Violation violation) {
    super(violation);
  }
  public AuthorizationException(String message, Throwable e) {
    super(message, e);
  }
  public AuthorizationException(String key, String message, Throwable e) {
    super(key, message, e);
  }
  public AuthorizationException(Violation violation, Throwable e) {
    super(violation, e);
  }
}

