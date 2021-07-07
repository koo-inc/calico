package jp.co.freemind.calico.core.util.type;

public class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> extends Tuple7<T1, T2, T3, T4, T5, T6, T7> {
  private final T8 value8;

  public Tuple8(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8) {
    super(value1, value2, value3, value4, value5, value6, value7);
    this.value8 = value8;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple8)) return false;
    final Tuple8 other = (Tuple8) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value8 = this.value8;
    final Object other$value8 = other.value8;
    if (this$value8 == null ? other$value8 != null : !this$value8.equals(other$value8)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value8 = this.value8;
    result = result * PRIME + ($value8 == null ? 43 : $value8.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple8;
  }

  public String toString() {
    return "Tuple8(super=" + super.toString() + ", value8=" + this.value8 + ")";
  }

  public T8 getValue8() {
    return this.value8;
  }
}
