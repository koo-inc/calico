package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.validation.Violation;
import lombok.Getter;

public abstract class VerificationException extends RuntimeException {
  @Getter
  private final Violation violation;

  public VerificationException(String message){
    this(new Violation(message));
  }
  public VerificationException(String key, String message){
    this(new Violation(key, message));
  }
  public VerificationException(Violation violation) {
    this.violation = violation;
  }
}
