package jp.co.freemind.calico.core.validation;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class Violation {
  private final Map<String, List<String>> errors = new LinkedHashMap<>();
  private final String[] stack;

  public Violation() {
    this(new String[0]);
  }
  public Violation(@Nonnull String errorCode) {
    this(new String[0]);
    this.mark("", errorCode);
  }
  public Violation(@Nonnull String key, @Nonnull String... errorCodes) {
    this(new String[0]);
    this.mark(key, new ArrayList<>(Arrays.asList(errorCodes)));
  }
  private Violation(String[] stack) {
    this.stack = stack;
  }

  public Violation mark(@Nonnull String key, @Nonnull String errorCode) {
    List<String> errors = this.errors.computeIfAbsent(getPath(key), path -> new ArrayList<>());
    errors.add(Objects.requireNonNull(errorCode));
    return this;
  }
  public Violation mark(@Nonnull String key, @Nonnull List<String> errorCodes) {
    List<String> errors = this.errors.computeIfAbsent(getPath(key), path -> new ArrayList<>());
    errorCodes.forEach(errorCode -> errors.add(Objects.requireNonNull(errorCode)));
    return this;
  }
  public Violation mark(int key, @Nonnull String errorCode) {
    return this.mark(String.valueOf(key), errorCode);
  }
  public Violation mark(@Nonnull String errorCode) {
    return mark("", errorCode);
  }

  public Violation referTo(@Nonnull String key, Consumer<Violation> consumer) {
    String[] newStack = Arrays.copyOf(stack, stack.length + 1);
    newStack[newStack.length - 1] = key;
    Violation child = new Violation(newStack);
    consumer.accept(child);
    child.errors.forEach(this::mark);
    return this;
  }
  public Violation referTo(@Nonnull String key, Supplier<Violation> supplier) {
    Violation childViolation = supplier.get();
    return referTo(key, violation -> childViolation.errors.forEach(violation::mark));
  }

  public Violation referTo(@Nonnull String key, int index, Consumer<Violation> consumer) {
    return referTo(String.valueOf(key) + "." + index, consumer);
  }
  public Violation referTo(@Nonnull String key, int index, Supplier<Violation> supplier) {
    return referTo(String.valueOf(key) + "." + index, supplier);
  }
  public Violation referTo(@Nonnull String key, @Nonnull String key2, Consumer<Violation> consumer) {
    return referTo(String.valueOf(key) + "." + key2, consumer);
  }
  public Violation referTo(@Nonnull String key, @Nonnull String key2, Supplier<Violation> supplier) {
    return referTo(String.valueOf(key) + "." + key2, supplier);
  }

  public boolean isFound() {
    return !errors.isEmpty();
  }

  private String getPath(String key) {
    Stream<String> keyStream = Strings.isNullOrEmpty(key) ? Stream.empty() : Stream.of(key);
    return Stream.concat(Arrays.stream(this.stack), keyStream)
      .collect(joining("."));
  }

  @JsonValue
  public Map<String, List<String>> toMap() {
    Map<String, List<String>> map = new LinkedHashMap<>();
    errors.forEach((key, value) -> map.put(key, ImmutableList.copyOf(value)));
    return Collections.unmodifiableMap(map);
  }

  @Override
  public String toString() {
    return "Violation{errors=" + errors + '}';
  }
}
