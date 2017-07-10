package jp.co.freemind.calico.core.util.type;

@Deprecated
public class Pair<L, R> extends Tuple2<L, R>{
  private Pair(L left, R right) {
    super(left, right);
  }

  public static <LEFT, RIGHT> Pair<LEFT, RIGHT> of (LEFT left, RIGHT right) {
    return new Pair<>(left, right);
  }

  public L getLeft() {
    return getValue1();
  }
  public R getRight() {
    return getValue2();
  }
}
