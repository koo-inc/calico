package jp.co.freemind.calico.core.util.type;

public class Tuple7<T1, T2, T3, T4, T5, T6, T7> extends Tuple6<T1, T2, T3, T4, T5, T6> {
  private final T7 value7;

  public Tuple7(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7) {
    super(value1, value2, value3, value4, value5, value6);
    this.value7 = value7;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple7)) return false;
    final Tuple7 other = (Tuple7) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value7 = this.value7;
    final Object other$value7 = other.value7;
    if (this$value7 == null ? other$value7 != null : !this$value7.equals(other$value7)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value7 = this.value7;
    result = result * PRIME + ($value7 == null ? 43 : $value7.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple7;
  }

  public String toString() {
    return "Tuple7(super=" + super.toString() + ", value7=" + this.value7 + ")";
  }

  public T7 getValue7() {
    return this.value7;
  }
}
