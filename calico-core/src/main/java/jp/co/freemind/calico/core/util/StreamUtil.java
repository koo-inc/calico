package jp.co.freemind.calico.core.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jp.co.freemind.calico.core.util.type.Tuple;
import jp.co.freemind.calico.core.util.type.Tuple2;


public class StreamUtil {

  public static <T, U> Stream<Tuple2<T, U>> zip(Stream<T> stream1, Stream<U> stream2) {
    return zip(stream1::iterator, stream2::iterator);
  }

  public static <T, U> Stream<Tuple2<T, U>> zip(Stream<T> stream1, Iterable<U> itr2) {
    return zip(stream1::iterator, itr2);
  }

  public static <T, U> Stream<Tuple2<T, U>> zip(Iterable<T> itr1, Stream<U> stream2) {
    return zip(itr1, stream2::iterator);
  }

  public static <T, U> Stream<Tuple2<T, U>> zip(Iterable<T> itr1, Iterable<U> itr2) {
    return StreamSupport.stream(
      Spliterators.spliteratorUnknownSize(
        new ZipIterator<T, U>(itr1.iterator(), itr2.iterator(), Tuple::<T, U>of),
        Spliterator.IMMUTABLE | Spliterator.NONNULL),
      false);
  }

  public static <T> void forEachWithIndex(Stream<T> stream, BiConsumer<T, Integer> consumer) {
    zip(stream, Stream.iterate(0, i -> ++i))
      .forEach(pair -> consumer.accept(pair.getValue1(), pair.getValue2()));
  }

  @SafeVarargs
  public static <T> Stream<T> of(T... ts) {
    return ts != null ? Stream.of(ts) : Stream.empty();
  }
  public static <T> Stream<T> of(Iterable<T> iterable) {
    return iterable != null ? StreamSupport.stream(iterable.spliterator(), false) : Stream.empty();
  }
  public static <T> Stream<T> of(Enumeration<T> enumeration) {
    return enumeration != null ? Collections.list(enumeration).stream() : Stream.empty();
  }

  private static class ZipIterator<T, U> implements Iterator<Tuple2<T, U>> {
    private final Iterator<T> itr1;
    private final Iterator<U> itr2;
    private final BiFunction<T, U, Tuple2<T, U>> mapper;

    @java.beans.ConstructorProperties({"itr1", "itr2", "mapper"})
    public ZipIterator(Iterator<T> itr1, Iterator<U> itr2, BiFunction<T, U, Tuple2<T, U>> mapper) {
      this.itr1 = itr1;
      this.itr2 = itr2;
      this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
      return itr1.hasNext() && itr2.hasNext();
    }

    @Override
    public Tuple2<T, U> next() {
      return mapper.apply(itr1.next(), itr2.next());
    }

  }
}
