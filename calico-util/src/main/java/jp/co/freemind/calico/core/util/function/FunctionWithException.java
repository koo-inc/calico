package jp.co.freemind.calico.core.util.function;

import java.util.function.Function;

import jp.co.freemind.calico.core.util.Throwables;

/**
 * Created by kakusuke on 15/06/12.
 */
@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
  R apply(T t) throws E;

  default Function<T, R> suppressCompileError() {
    return (t) -> {
      try {
        return apply(t);
      } catch (Exception e) {
        throw Throwables.sneakyThrow(e);
      }
    };
  }
}
