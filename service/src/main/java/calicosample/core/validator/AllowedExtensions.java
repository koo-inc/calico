package calicosample.core.validator;

import jp.co.freemind.calico.media.Media;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by kakusuke on 15/05/13.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AllowedExtensions.ExtensionValidator.class})
public @interface AllowedExtensions {
  String message() default "{calico.constraints.AllowedExtensions.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  String[] value();

  class ExtensionValidator implements ConstraintValidator<AllowedExtensions, Media> {

    private List<String> extensions;

    @Override
    public void initialize(AllowedExtensions allowedExtensions) {
      this.extensions = Stream.of(allowedExtensions.value())
        .map(ext -> "." + ext)
        .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Media media, ConstraintValidatorContext constraintValidatorContext) {
      // nullの場合はOK、欠損がある場合はNGとする
      if (media == null) return true;
      if (media.getMeta() == null) return false;
      if (media.getMeta().getName() == null) return false;
      return extensions.stream().anyMatch(media.getMeta().getName()::endsWith);
    }
  }
}
