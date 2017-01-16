package jp.co.freemind.calico.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.beanutils.BeanUtils;

public class DTOUtil {

  public static <T, E> T copyProperties(T dest, E orig){
    try {
      BeanUtils.copyProperties(dest, orig);
    } catch (IllegalAccessException | InvocationTargetException e) {}
    return dest;
  }

  public static <T, E> Function<T, E> toInstanceOf(Supplier<E> dest) {
    return (orig) -> copyProperties(dest.get(), orig);
  }
}
