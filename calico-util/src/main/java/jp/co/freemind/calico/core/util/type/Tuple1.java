package jp.co.freemind.calico.core.util.type;

public class Tuple1<T1> implements Tuple {
  private final T1 value1;

  @java.beans.ConstructorProperties({"value1"})
  public Tuple1(T1 value1) {
    this.value1 = value1;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple1)) return false;
    final Tuple1 other = (Tuple1) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$value1 = this.value1;
    final Object other$value1 = other.value1;
    if (this$value1 == null ? other$value1 != null : !this$value1.equals(other$value1)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $value1 = this.value1;
    result = result * PRIME + ($value1 == null ? 43 : $value1.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple1;
  }

  public T1 getValue1() {
    return this.value1;
  }
}
