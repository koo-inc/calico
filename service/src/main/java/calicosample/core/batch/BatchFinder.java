package calicosample.core.batch;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.util.ClassFinder;

/**
 * Created by kakusuke on 15/07/16.
 */
public class BatchFinder {
  @SuppressWarnings("unchecked")
  public static List<Class<? extends Batch>> findAll() {
    return ClassFinder.findClasses(Env.getRootPackage() + ".batch").stream()
      .filter(Batch.class::isAssignableFrom)
      .filter(c -> c.isAnnotationPresent(BatchIndex.class))
      .map(c -> (Class<? extends Batch>) c)
      .sorted(Comparator.comparing(c -> c.getAnnotation(BatchIndex.class).index()))
      .collect(toList());
  }

  public static Class<? extends Batch> find(String index) {
    return findAll().stream()
      .filter(c -> Objects.equals(c.getAnnotation(BatchIndex.class).index(), index))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("指定されたバッチは存在しません (index = " + index + ")"));
  }
}
