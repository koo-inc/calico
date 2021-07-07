package jp.co.freemind.calico.core.endpoint.validation;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.RawField;
import com.fasterxml.classmate.members.RawMethod;
import jp.co.freemind.calico.core.validation.Violation;

public class Validator {
  private final Collection<? extends Validation<?>> validations;
  private final Collection<? extends NestingValidation<?>> nestedValidations;

  public static ValidatorFactory factory() {
    return new ValidatorFactory();
  }

  public Validator(Collection<? extends Validation<?>> validations, Collection<? extends NestingValidation<?>> nestedValidations) {
    this.validations = validations;
    this.nestedValidations = nestedValidations;
  }

  public Violation validate(Class<?> type, Object input) {
    Violation violation = new Violation();
    validate(violation, type, input);
    return violation;
  }

  private void validate(Violation violation, Class<?> type, Object input) {
    for (FieldAccessor field : getFieldAccessors(type)) {
      validateField(violation, field, field.getValue(input));
    }
    for (MethodAccessor method : getEagerValidateMethods(type)) {
      method.invoke(input, violation);
    }
    if (!violation.isFound()) {
      for (MethodAccessor method : getLazyValidateMethods(type)) {
        method.invoke(input, violation);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void validateField(Violation violation, FieldAccessor field, Object value) {
    for (Validation validation : validations) {
      if (!validation.matches(field)) continue;
      if (!validation.validate(field, value)) {
        violation.mark(field.getName(), validation.getErrorMessage(field, value));
      }
    }

    for (NestingValidation nestingValidation : nestedValidations) {
      if (!nestingValidation.matches(field)) continue;
      nestingValidation.validate(field, value, this, violation);
    }
  }

  private static TypeResolver typeResolver = new TypeResolver();

  private static final Map<Class<?>, Set<FieldAccessor>> fieldAccessorsMap = new ConcurrentHashMap<>();
  private static Set<FieldAccessor> getFieldAccessors(@Nonnull Class<?> type) {
    return fieldAccessorsMap.computeIfAbsent(type, (t) -> {
      ResolvedType target = typeResolver.resolve(type);
      Map<String, FieldAccessor> fieldMap = new LinkedHashMap<>();
      while(target != null) {
        for (RawField field : target.getMemberFields()) {
          FieldAccessor fieldAccessor = new FieldAccessor(resolveType(target, field), field);
          fieldMap.putIfAbsent(field.getName(), fieldAccessor);
        }
        target = target.getParentClass();
      }
      return new LinkedHashSet<>(fieldMap.values());
    });
  }

  private static final Map<Class<?>, Set<MethodAccessor>> eagerValidateMethodMap = new ConcurrentHashMap<>();
  private static Set<MethodAccessor> getEagerValidateMethods(@Nonnull Class<?> type) {
    return getValidateMethods(type, eagerValidateMethodMap, Validate::eager);
  }

  private static final Map<Class<?>, Set<MethodAccessor>> lazyValidateMethodMap = new ConcurrentHashMap<>();
  private static Set<MethodAccessor> getLazyValidateMethods(@Nonnull Class<?> type) {
    return getValidateMethods(type, lazyValidateMethodMap, anno -> !anno.eager());
  }

  private static Set<MethodAccessor> getValidateMethods(@Nonnull Class<?> type, @Nonnull Map<Class<?>, Set<MethodAccessor>> cacheMap, @Nonnull Predicate<Validate> filter) {
    return cacheMap.computeIfAbsent(type, t -> {
      ResolvedType target = typeResolver.resolve(type);
      Set<MethodAccessor> methods = new LinkedHashSet<>();
      while(target != null) {
        for (RawMethod method : target.getMemberMethods()) {
          Validate anno = method.getRawMember().getAnnotation(Validate.class);
          if (anno == null || !filter.test(anno)) continue;
          methods.add(new MethodAccessor(method));
        }
        target = target.getParentClass();
      }
      return methods;
    });
  }

  private static ResolvedType resolveType(@Nonnull ResolvedType targetType, @Nonnull RawField field) {
    Type genericType = field.getRawMember().getGenericType();
    if (!(genericType instanceof TypeVariable)) {
      return typeResolver.resolve(field.getRawMember().getType());
    }

    List<ResolvedType> typeParameters = targetType.getTypeParameters();
    Type[] rawTypeParams = field.getRawMember().getDeclaringClass().getTypeParameters();
    for (int i = 0, len = rawTypeParams.length; i < len; i++) {
      if (rawTypeParams[i] == genericType) {
        return typeParameters.get(i);
      }
    }
    throw new IllegalArgumentException("Cannot resolve " + field + " from " + targetType);
  }
}
