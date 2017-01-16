package jp.co.freemind.calico.service.exception;

import java.util.Map;

public class InvalidUserDataException extends ServiceException {
  public InvalidUserDataException(String key, String message) {
    super(key, message);
  }
  public InvalidUserDataException(Map<String, String> violations) {
    violations.forEach(this::add);
  }
}
