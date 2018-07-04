package jp.co.freemind.calico.core.util.type;

public class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> {
  private final T9 value9;

  public Tuple9(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9) {
    super(value1, value2, value3, value4, value5, value6, value7, value8);
    this.value9 = value9;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple9)) return false;
    final Tuple9 other = (Tuple9) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value9 = this.value9;
    final Object other$value9 = other.value9;
    if (this$value9 == null ? other$value9 != null : !this$value9.equals(other$value9)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value9 = this.value9;
    result = result * PRIME + ($value9 == null ? 43 : $value9.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple9;
  }

  public String toString() {
    return "Tuple9(super=" + super.toString() + ", value9=" + this.value9 + ")";
  }

  public T9 getValue9() {
    return this.value9;
  }
}
