package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.endpoint.validation.Message;

public class AuthorizationException extends VerificationException {
  public AuthorizationException(String message) {
    super(message);
  }
  public AuthorizationException(Message message) {
    this(message.value());
  }
}

