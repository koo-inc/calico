package jp.co.freemind.calico.core.orm;

import java.util.Objects;

import com.fasterxml.classmate.TypeResolver;
import com.sun.istack.internal.NotNull;

public abstract class Identifier<T> {
  private static final TypeResolver resolver = new TypeResolver();
  private final T value;

  public Identifier(@NotNull T value) {
    Objects.requireNonNull(value);
    this.value = value;
  }
  public @NotNull T getValue() {
    return value;
  }

  @SuppressWarnings("unchecked")
  public Class<T> getValueClass() {
    return (Class<T>) resolver.resolve(this.getClass()).typeParametersFor(Identifier.class).get(0).getErasedType();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Identifier<?> that = (Identifier<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
