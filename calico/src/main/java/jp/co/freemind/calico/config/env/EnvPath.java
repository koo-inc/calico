package jp.co.freemind.calico.config.env;

import java.lang.annotation.*;

/**
 * Created by tasuku on 15/04/09.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnvPath {
  String value();
}
