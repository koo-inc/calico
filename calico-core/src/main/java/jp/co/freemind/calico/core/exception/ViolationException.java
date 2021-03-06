package jp.co.freemind.calico.core.exception;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.validation.Violation;

public abstract class ViolationException extends RuntimeException {
  private final Violation violation;

  public ViolationException(Message message, Object... args){
    this(message.value(args));
  }
  public ViolationException(String message){
    this(new Violation(message));
  }
  public ViolationException(String key, String message){
    this(new Violation(key, message));
  }
  public ViolationException(Violation violation) {
    super(violation != null ? violation.toString() : null);
    this.violation = violation;
  }
  public ViolationException(String message, Throwable e){
    this(new Violation(message), e);
  }
  public ViolationException(String key, String message, Throwable e) {
    this(new Violation(key, message), e);
  }
  public ViolationException(Violation violation, Throwable e){
    super(e);
    this.violation = violation;
  }

  public Violation getViolation() {
    return this.violation;
  }
}
