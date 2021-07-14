package jp.co.freemind.calico.core.endpoint.validation;

import java.lang.annotation.Annotation;

import com.fasterxml.classmate.members.RawMethod;
import jp.co.freemind.calico.core.util.Throwables;

public class MethodAccessor {
  private final RawMethod method;

  public MethodAccessor(RawMethod method) {
    this.method = method;
  }

  public Object invoke(Object instance, Object... args) {
    if (!method.isPublic()) {
      method.getRawMember().setAccessible(true);
    }
    try {
      return method.getRawMember().invoke(instance, args);
    } catch (ReflectiveOperationException e) {
      throw Throwables.sneakyThrow(e);
    }
  }

  public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
    return method.getRawMember().isAnnotationPresent(annotation);
  }
}
