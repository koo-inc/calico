package jp.co.freemind.calico.core.endpoint.validation;

@FunctionalInterface
public interface FieldAccessorMatcher {
  boolean matches(FieldAccessor fieldAccessor);
}
