package jp.co.freemind.calico.service.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class InconsistentDataException extends ServiceException {
  public InconsistentDataException(String key, String message) {
    super(key, message);
  }
  public <T> InconsistentDataException(Set<ConstraintViolation<T>> violations) {
    super(violations);
  }
}
