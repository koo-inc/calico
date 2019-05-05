package jp.co.freemind.calico.core.util.type;

public class Tuple6<T1, T2, T3, T4, T5, T6> extends Tuple5<T1, T2, T3, T4, T5> {
  private final T6 value6;

  public Tuple6(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6) {
    super(value1, value2, value3, value4, value5);
    this.value6 = value6;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple6)) return false;
    final Tuple6 other = (Tuple6) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value6 = this.value6;
    final Object other$value6 = other.value6;
    if (this$value6 == null ? other$value6 != null : !this$value6.equals(other$value6)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value6 = this.value6;
    result = result * PRIME + ($value6 == null ? 43 : $value6.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple6;
  }

  public String toString() {
    return "Tuple6(super=" + super.toString() + ", value6=" + this.value6 + ")";
  }

  public T6 getValue6() {
    return this.value6;
  }
}
