package jp.co.freemind.calico.core.endpoint.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidatorFactory {
  private final List<Validation<?>> validations = new ArrayList<>();
  private final List<NestingValidation<?>> nestingValidations = new ArrayList<>(Arrays.asList(
    new NestingValidation.CollectionValidation<>(),
    new NestingValidation.MapValidation<>()
  ));

  public <T> Context<T> when(Predicate<FieldAccessor> predicate) {
    return new Context<>(predicate::test);
  }

  public <U> ValidatorFactory expected(Validation<U> validation) {
    validations.add(validation);
    return this;
  }

  public <U> ValidatorFactory expected(NestingValidation<U> nestingValidation) {
    nestingValidations.add(nestingValidation);
    return this;
  }

  public Validator getValidator() {
    return new Validator(validations, nestingValidations);
  }

  public class Context<T> {
    private final FieldAccessorMatcher matcher;

    public Context(FieldAccessorMatcher matcher) {
      this.matcher = matcher;
    }

    public ValidatorFactory then(BiFunction<FieldAccessor, T, Boolean> validateMethod, Function<FieldAccessor, Message> messageMapper) {
      return then(validateMethod, (f, t)-> messageMapper.apply(f));
    }
    public ValidatorFactory then(BiFunction<FieldAccessor, T, Boolean> validateMethod, Message message) {
      return then(validateMethod, f -> message);
    }
    public ValidatorFactory then(BiFunction<FieldAccessor, T, Boolean> validateMethod, BiFunction<FieldAccessor, T, Message> messageMapper) {
      Validation<T> validation = new Validation.SelectiveValidation<>(matcher, validateMethod, messageMapper);
      return expected(validation);
    }
    public ValidatorFactory thenNesting() {
      NestingValidation<T> nestingValidation = new NestingValidation.SelectiveValidation<T>(matcher);
      return expected(nestingValidation);
    }
  }
}
