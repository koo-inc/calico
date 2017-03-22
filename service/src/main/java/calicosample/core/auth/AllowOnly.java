package calicosample.core.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import calicosample.extenum.CalicoSampleAuthority;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Documented
public @interface AllowOnly {
  CalicoSampleAuthority[] value() default {};
}
