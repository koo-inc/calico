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
public class TextList extends JsonObject<List<String>> implements List<String> {
  private static final TextList EMPTY = new TextList(Collections.emptyList());
  public static TextList empty() { return EMPTY; }

  public TextList(List<String> object) {
    super(object);
  }
  public TextList(String json) {
    super(json);
  }

  public int size() {
    return get().size();
  }

  public Spliterator<String> spliterator() {
    return get().spliterator();
  }

  public <T> T[] toArray(T[] a) {
    return get().toArray(a);
  }

  public int indexOf(Object o) {
    return get().indexOf(o);
  }

  public Stream<String> stream() {
    return get().stream();
  }

  public void forEach(Consumer<? super String> action) {
    get().forEach(action);
  }

  public String set(int index, String element) {
    return get().set(index, element);
  }

  public boolean retainAll(Collection<?> c) {
    return get().retainAll(c);
  }

  public Object[] toArray() {
    return get().toArray();
  }

  public void replaceAll(UnaryOperator<String> operator) {
    get().replaceAll(operator);
  }

  public ListIterator<String> listIterator(int index) {
    return get().listIterator(index);
  }

  public ListIterator<String> listIterator() {
    return get().listIterator();
  }

  public boolean isEmpty() {
    return get().isEmpty();
  }

  public boolean removeAll(Collection<?> c) {
    return get().removeAll(c);
  }

  public boolean addAll(Collection<? extends String> c) {
    return get().addAll(c);
  }

  public Iterator<String> iterator() {
    return get().iterator();
  }

  public boolean contains(Object o) {
    return get().contains(o);
  }

  public int lastIndexOf(Object o) {
    return get().lastIndexOf(o);
  }

  public void sort(Comparator<? super String> c) {
    get().sort(c);
  }

  public boolean containsAll(Collection<?> c) {
    return get().containsAll(c);
  }

  public String get(int index) {
    return get().get(index);
  }

  public void add(int index, String element) {
    get().add(index, element);
  }

  public void clear() {
    get().clear();
  }

  public String remove(int index) {
    return get().remove(index);
  }

  public boolean add(String s) {
    return get().add(s);
  }

  public Stream<String> parallelStream() {
    return get().parallelStream();
  }

  public boolean removeIf(Predicate<? super String> filter) {
    return get().removeIf(filter);
  }

  public boolean addAll(int index, Collection<? extends String> c) {
    return get().addAll(index, c);
  }

  public boolean remove(Object o) {
    return get().remove(o);
  }

  public List<String> subList(int fromIndex, int toIndex) {
    return get().subList(fromIndex, toIndex);
  }
}
