package jp.co.freemind.calico.core.endpoint.validation;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Validation<T> {
  boolean matches(FieldAccessor field);
  boolean validate(FieldAccessor field, T object);
  String getErrorMessage(FieldAccessor field);

  static ValidatorFactory factory() {
    return new ValidatorFactory();
  }

  class SelectiveValidation<U> implements Validation<U> {
    private final FieldAccessorMatcher matcher;
    private final BiFunction<FieldAccessor, U, Boolean> validateMethod;
    private final Function<FieldAccessor, Message> messageMapper;

    SelectiveValidation(FieldAccessorMatcher matcher, BiFunction<FieldAccessor, U, Boolean> validateMethod, Function<FieldAccessor, Message> messageMapper) {
      this.matcher = matcher;
      this.validateMethod = validateMethod;
      this.messageMapper = messageMapper;
    }

    @Override
    public boolean matches(FieldAccessor field) {
      return matcher.matches(field);
    }

    @Override
    public boolean validate(FieldAccessor field, U object) {
      return validateMethod.apply(field, object);
    }

    @Override
    public String getErrorMessage(FieldAccessor field) {
      return messageMapper.apply(field).value(field);
    }
  }

  class DefaultValidation<T> implements Validation<T> {
    private final BiFunction<FieldAccessor, T, Boolean> validateMethod;
    private final String message;

    DefaultValidation(BiFunction<FieldAccessor, T, Boolean> validateMethod, String message) {
      this.validateMethod = validateMethod;
      this.message = message;
    }

    @Override
    public boolean matches(FieldAccessor field) {
      return true;
    }

    @Override
    public boolean validate(FieldAccessor field, T object) {
      return validateMethod.apply(field, object);
    }

    @Override
    public String getErrorMessage(FieldAccessor field) {
      return message;
    }
  }
}
