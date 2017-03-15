package jp.co.freemind.calico.core.util.function;

/**
 * Created by kakusuke on 15/06/11.
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
  void accept(A a, B b, C c);
}
