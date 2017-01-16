package calicosample.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^[1-9-]{5,20}$", message = "{calico.constraints.PhoneNumber.message}")
public @interface PhoneNumber {
  String message() default "{calico.constraints.PhoneNumber.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    PhoneNumber[] value();
  }


}
