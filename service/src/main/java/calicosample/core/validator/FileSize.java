package calicosample.core.validator;

import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaProxy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by kakusuke on 15/05/13.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {FileSize.FileSizeValidator.class})
public @interface FileSize {
  String message() default "{calico.constraints.FileSize.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  long max() default 0;
  long min() default 0;

  class FileSizeValidator implements ConstraintValidator<FileSize, Media> {

    private long max;
    private long min;

    @Override
    public void initialize(FileSize fileSize) {
      this.max = fileSize.max();
      this.min = fileSize.min();
    }

    @Override
    public boolean isValid(Media media, ConstraintValidatorContext constraintValidatorContext) {
      // nullの場合はOK、欠損がある場合はNGとする
      if (media == null) return true;
      if (media.getMeta() == null) return false;
      if (media.getMeta().getSize() == null) return false;

      long size = media instanceof MediaProxy ? media.getMeta().getSize() : media.getPayload().length;
      if (size > max) return false;
      if (size < min) return false;
      return true;
    }
  }
}
