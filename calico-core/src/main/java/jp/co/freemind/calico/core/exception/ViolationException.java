package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.validation.Violation;
import lombok.Getter;

public abstract class ViolationException extends RuntimeException {
  @Getter
  private final Violation violation;

  public ViolationException(String message){
    this(new Violation(message));
  }
  public ViolationException(String key, String message){
    this(new Violation(key, message));
  }
  public ViolationException(Violation violation) {
    this.violation = violation;
  }
}
