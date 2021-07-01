package jp.co.freemind.calico.core.endpoint.validation;

import java.util.Collection;
import java.util.Map;

import jp.co.freemind.calico.core.validation.Violation;

public interface NestingValidation<T> {
  boolean matches(FieldAccessor field);
  Violation validate(FieldAccessor field, T object, Validator validator, Violation violation);

  static ValidatorFactory factory() {
    return new ValidatorFactory();
  }

  class CollectionValidation<U> implements NestingValidation<Collection<U>> {

    @Override
    public boolean matches(FieldAccessor field) {
      return field.isInheritedFrom(Collection.class);
    }

    @Override
    public Violation validate(FieldAccessor field, Collection<U> value, Validator validator, Violation violation) {
      if (value == null) return violation;

      int i = 0;
      for (Object component : value) {
        int index = i++;
        Class<?> type = component != null ? component.getClass() : Object.class;
        violation.referTo(field.getName(), index, () -> validator.validate(type, component));
      }
      return violation;
    }
  }

  class MapValidation<T, U> implements NestingValidation<Map<T, U>> {

    @Override
    public boolean matches(FieldAccessor field) {
      return field.isInheritedFrom(Map.class);
    }

    @Override
    public Violation validate(FieldAccessor field, Map<T, U> value, Validator validator, Violation violation) {
      if (value == null) return violation;

      for (Map.Entry<T, U> e : value.entrySet()) {
        String key = String.valueOf(e.getKey() != null ? e.getKey() : "");
        Class<?> type = e.getValue() != null ? e.getValue().getClass() : Object.class;
        violation.referTo(field.getName(), key, () ->
          validator.validate(type, e.getValue()));
      }
      return violation;
    }
  }

  class SelectiveValidation<U> implements NestingValidation<U> {
    private final FieldAccessorMatcher matcher;

    SelectiveValidation(FieldAccessorMatcher matcher) {
      this.matcher = matcher;
    }

    @Override
    public boolean matches(FieldAccessor field) {
      return matcher.matches(field);
    }

    @Override
    public Violation validate(FieldAccessor field, U value, Validator validator, Violation violation) {
      if (value == null) return violation;
      violation.referTo(field.getName(), () -> validator.validate(value.getClass(), value));
      return violation;
    }
  }
}
