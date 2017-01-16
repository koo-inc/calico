package calicosample.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.seasar.doma.Domain;
import jp.co.freemind.calico.json.JsonObject;

/**
 * Created by kakusuke on 15/08/05.
 */
@Domain(valueType = String.class)
public class IntList extends JsonObject<List<Integer>> implements List<Integer> {
  private static final IntList EMPTY = new IntList(Collections.emptyList());
  public static IntList empty() { return EMPTY; }

  public IntList(List<Integer> object) {
    super(object);
  }
  public IntList(String json) {
    super(json);
  }

  public int size() {
    return get().size();
  }

  public Spliterator<Integer> spliterator() {
    return get().spliterator();
  }

  public <T> T[] toArray(T[] a) {
    return get().toArray(a);
  }

  public int indexOf(Object o) {
    return get().indexOf(o);
  }

  public Stream<Integer> stream() {
    return get().stream();
  }

  public void forEach(Consumer<? super Integer> action) {
    get().forEach(action);
  }

  public Integer set(int index, Integer element) {
    return get().set(index, element);
  }

  public boolean retainAll(Collection<?> c) {
    return get().retainAll(c);
  }

  public Object[] toArray() {
    return get().toArray();
  }

  public void replaceAll(UnaryOperator<Integer> operator) {
    get().replaceAll(operator);
  }

  public ListIterator<Integer> listIterator(int index) {
    return get().listIterator(index);
  }

  public ListIterator<Integer> listIterator() {
    return get().listIterator();
  }

  public boolean isEmpty() {
    return get().isEmpty();
  }

  public boolean removeAll(Collection<?> c) {
    return get().removeAll(c);
  }

  public boolean addAll(Collection<? extends Integer> c) {
    return get().addAll(c);
  }

  public Iterator<Integer> iterator() {
    return get().iterator();
  }

  public boolean contains(Object o) {
    return get().contains(o);
  }

  public int lastIndexOf(Object o) {
    return get().lastIndexOf(o);
  }

  public void sort(Comparator<? super Integer> c) {
    get().sort(c);
  }

  public boolean containsAll(Collection<?> c) {
    return get().containsAll(c);
  }

  public Integer get(int index) {
    return get().get(index);
  }

  public void add(int index, Integer element) {
    get().add(index, element);
  }

  public void clear() {
    get().clear();
  }

  public Integer remove(int index) {
    return get().remove(index);
  }

  public boolean add(Integer integer) {
    return get().add(integer);
  }

  public Stream<Integer> parallelStream() {
    return get().parallelStream();
  }

  public boolean removeIf(Predicate<? super Integer> filter) {
    return get().removeIf(filter);
  }

  public boolean addAll(int index, Collection<? extends Integer> c) {
    return get().addAll(index, c);
  }

  public boolean remove(Object o) {
    return get().remove(o);
  }

  public List<Integer> subList(int fromIndex, int toIndex) {
    return get().subList(fromIndex, toIndex);
  }
}
