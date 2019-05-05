package jp.co.freemind.calico.core.util.type;

public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
  private final T4 value4;

  public Tuple4(T1 value1, T2 value2, T3 value3, T4 value4) {
    super(value1, value2, value3);
    this.value4 = value4;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple4)) return false;
    final Tuple4 other = (Tuple4) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value4 = this.value4;
    final Object other$value4 = other.value4;
    if (this$value4 == null ? other$value4 != null : !this$value4.equals(other$value4)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value4 = this.value4;
    result = result * PRIME + ($value4 == null ? 43 : $value4.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple4;
  }

  public String toString() {
    return "Tuple4(super=" + super.toString() + ", value4=" + this.value4 + ")";
  }

  public T4 getValue4() {
    return this.value4;
  }
}
