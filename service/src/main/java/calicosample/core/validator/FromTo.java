package calicosample.core.validator;

import org.apache.commons.lang3.tuple.Pair;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Optional;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {FromTo.LocalDateValidator.class, FromTo.LocalTimeValidator.class, FromTo.LocalDateTimeValidator.class})
public @interface FromTo {
  String message() default "{calico.constraints.FromTo.date.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  boolean allowEqual() default true;
  @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    FromTo[] value();
  }

  class ValidatorBase<T extends Comparable<T>> {
    private boolean allowEqual;
    private Optional<String> template;

    ValidatorBase() {
      this.template = Optional.empty();
    }
    ValidatorBase(String template) {
      this.template = Optional.of(template);
    }

    public void initialize(FromTo constraintAnnotation) {
      allowEqual = constraintAnnotation.allowEqual();
    }
    public boolean isValid(T left, T right, ConstraintValidatorContext context) {
      if (left == null || right == null) {
        return true;
      }
      if (allowEqual && left.equals(right)) {
        return true;
      }
      if (left.compareTo(right) < 0) {
        return true;
      }
      template.ifPresent((t) -> {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(t).addConstraintViolation();
      });
      return false;
    }
  }

  class LocalDateValidator implements ConstraintValidator<FromTo, Pair<LocalDate, LocalDate>> {
    private ValidatorBase<ChronoLocalDate> base = new ValidatorBase<>();

    @Override
    public void initialize(FromTo constraintAnnotation) {
      base.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Pair<LocalDate, LocalDate> value, ConstraintValidatorContext context) {
      return base.isValid(value.getLeft(), value.getRight(), context);
    }
  }

  class LocalTimeValidator implements ConstraintValidator<FromTo, Pair<LocalTime, LocalTime>> {
    private ValidatorBase<LocalTime> base = new ValidatorBase<>("{calico.constraints.FromTo.time.message}");

    @Override
    public void initialize(FromTo constraintAnnotation) {
      base.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Pair<LocalTime, LocalTime> value, ConstraintValidatorContext context) {
      return base.isValid(value.getLeft(), value.getRight(), context);
    }
  }

  class LocalDateTimeValidator implements ConstraintValidator<FromTo, Pair<LocalDateTime, LocalDateTime>> {
    private ValidatorBase<ChronoLocalDateTime<?>> base = new ValidatorBase<>("{calico.constraints.FromTo.timestamp.message}");

    @Override
    public void initialize(FromTo constraintAnnotation) {
      base.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Pair<LocalDateTime, LocalDateTime> value, ConstraintValidatorContext context) {
      return base.isValid(value.getLeft(), value.getRight(), context);
    }
  }

}
