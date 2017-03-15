package jp.co.freemind.calico.core.util.type;

public class Pair<L, R> {
  private final L left;
  private final R right;

  private Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public static <LEFT, RIGHT> Pair<LEFT, RIGHT> of (LEFT left, RIGHT right) {
    return new Pair<>(left, right);
  }

  public L getLeft() {
    return left;
  }
  public R getRight() {
    return right;
  }
}
