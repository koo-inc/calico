package jp.co.freemind.calico.core.util.type;

public class Tuple2<T1, T2> extends Tuple1<T1> {
  private final T2 value2;

  public Tuple2(T1 value1, T2 value2) {
    super(value1);
    this.value2 = value2;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple2)) return false;
    final Tuple2 other = (Tuple2) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value2 = this.value2;
    final Object other$value2 = other.value2;
    if (this$value2 == null ? other$value2 != null : !this$value2.equals(other$value2)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value2 = this.value2;
    result = result * PRIME + ($value2 == null ? 43 : $value2.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple2;
  }

  public String toString() {
    return "Tuple2(super=" + super.toString() + ", value2=" + this.value2 + ")";
  }

  public T2 getValue2() {
    return this.value2;
  }
}
