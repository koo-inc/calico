package jp.co.freemind.calico.core.util.type;

public class Tuple5<T1, T2, T3, T4, T5> extends Tuple4<T1, T2, T3, T4> {
  private final T5 value5;

  public Tuple5(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
    super(value1, value2, value3, value4);
    this.value5 = value5;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple5)) return false;
    final Tuple5 other = (Tuple5) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value5 = this.value5;
    final Object other$value5 = other.value5;
    if (this$value5 == null ? other$value5 != null : !this$value5.equals(other$value5)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value5 = this.value5;
    result = result * PRIME + ($value5 == null ? 43 : $value5.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple5;
  }

  public String toString() {
    return "Tuple5(super=" + super.toString() + ", value5=" + this.value5 + ")";
  }

  public T5 getValue5() {
    return this.value5;
  }
}
