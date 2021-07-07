package jp.co.freemind.calico.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import jp.co.freemind.calico.core.util.function.TriConsumer;


public interface OptionalUtil {

  static <T> Optional<T> coalesce(List<Optional<T>> items) {
    return items.stream()
      .filter(Optional::isPresent)
      .findFirst()
      .map(Optional::get);
  }
  @SafeVarargs
  static <T> Optional<T> coalesce(Optional<T>... items) {
    return coalesce(Arrays.asList(items));
  }

  static <T> void ifPresent(Optional<T> a, Consumer<T> consumer) {
    a.ifPresent(consumer);
  }

  static <A, B> void ifPresentAll(Optional<A> a, Optional<B> b, BiConsumer<A, B> consumer) {
    ifPresent(a, (aVal) -> b.ifPresent(bVal -> consumer.accept(aVal, bVal)));
  }
  static <A, B, C> void ifPresentAll(Optional<A> a, Optional<B> b, Optional<C> c, TriConsumer<A, B, C> consumer) {
    ifPresentAll(a, b, (aVal, bVal) ->
      c.ifPresent(cVal -> consumer.accept(aVal, bVal, cVal)));
  }
  static <T> void ifPresentAll(List<Optional<T>> optionals, Consumer<List<T>> consumer) {
    if (optionals.stream().allMatch(Optional::isPresent)) {
      List<T> list = optionals.stream().map(Optional::get).collect(Collectors.toList());
      consumer.accept(list);
    }
  }
  static <T> void ifPresentAll(Optional<T>[] optionals, Consumer<List<T>> consumer) {
    ifPresentAll(Arrays.asList(optionals), consumer);
  }

  static <T> void ifAbsent(Optional<T> t, Runnable runnable) {
    if (!t.isPresent()) {
      runnable.run();
    }
  }
  static <A, B> void ifAbsentAll(Optional<A> a, Optional<B> b, Runnable runnable) {
    ifAbsent(a, () -> ifAbsent(b, runnable));
  }
  static <A, B, C> void ifAbsentAll(Optional<A> a, Optional<B> b, Optional<C> c, Runnable runnable) {
    ifAbsentAll(a, b, () -> ifAbsent(c, runnable));
  }
  static <T> void ifAbsentAll(List<Optional<T>> optionals, Runnable runnable) {
    if (optionals.stream().filter(Optional::isPresent).findFirst().isPresent()) return;
    runnable.run();
  }
  static <T> void ifAbsentAll(Optional<T>[] optionals, Runnable runnable) {
    ifAbsentAll(Arrays.asList(optionals), runnable);
  }

  static Optional<Integer> boxing(OptionalInt optionalInt) {
    if (optionalInt.isPresent()) {
      return Optional.of(optionalInt.getAsInt());
    }
    else {
      return Optional.empty();
    }
  }

  static Optional<Long> boxing(OptionalLong optionalLong) {
    if (optionalLong.isPresent()) {
      return Optional.of(optionalLong.getAsLong());
    }
    else {
      return Optional.empty();
    }
  }

  static Optional<Double> boxing(OptionalDouble optionalDouble) {
    if (optionalDouble.isPresent()) {
      return Optional.of(optionalDouble.getAsDouble());
    }
    else {
      return Optional.empty();
    }
  }

}
