package jp.co.freemind.calico.core.util;

import java.util.function.Function;

import jp.co.freemind.calico.core.util.function.FunctionWithException;

/**
 * Created by kakusuke on 15/06/12.
 */
public interface FunctionUtil {
  static <T, R, E extends Exception> Function<T, R> silence(FunctionWithException<T, R, E> function) {
    return function.suppressCompileError();
  }
}
