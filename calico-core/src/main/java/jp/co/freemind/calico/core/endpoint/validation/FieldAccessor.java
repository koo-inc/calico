package jp.co.freemind.calico.core.endpoint.validation;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.RawField;
import jp.co.freemind.calico.core.util.Throwables;

public class FieldAccessor {
  private final ResolvedType type;
  private final RawField field;

  public FieldAccessor(@Nonnull ResolvedType type, RawField field) {
    this.type = type;
    this.field = field;
  }

  public String getName() {
    return field.getName();
  }

  public Object getValue(Object instance) {
    if (!field.isPublic()) {
      field.getRawMember().setAccessible(true);
    }
    try {
      return field.getRawMember().get(instance);
    } catch (IllegalAccessException e) {
      throw Throwables.sneakyThrow(e);
    }
  }

  public ResolvedType getResolvedType() {
    return type;
  }

  public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
    return field.getRawMember().isAnnotationPresent(annotation);
  }

  public Annotation[] getAnnotations() {
    return field.getRawMember().getAnnotations();
  }
  public @Nullable <A extends Annotation> A getAnnotation(Class<A> annotation) {
    return field.getRawMember().getAnnotation(annotation);
  }

  public boolean is(Class<?> rawType) {
    return type.getErasedType() == rawType;
  }

  @Override
  public String toString() {
    return "FieldAccessor{type=" + type + ", field=" + field + '}';
  }

  public static Predicate<FieldAccessor> annotatedWith(Class<? extends Annotation> annotation) {
    return fa -> fa.field.getRawMember().isAnnotationPresent(annotation);
  }

  public static Predicate<FieldAccessor> typeOf(Class<?> type) {
    return fa -> fa.is(type);
  }
}
