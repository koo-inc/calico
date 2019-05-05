package jp.co.freemind.calico.core.util.type;

public class Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> extends Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
  private final T17 value17;

  public Tuple17(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16);
    this.value17 = value17;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple17)) return false;
    final Tuple17 other = (Tuple17) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value17 = this.value17;
    final Object other$value17 = other.value17;
    if (this$value17 == null ? other$value17 != null : !this$value17.equals(other$value17)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value17 = this.value17;
    result = result * PRIME + ($value17 == null ? 43 : $value17.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple17;
  }

  public String toString() {
    return "Tuple17(super=" + super.toString() + ", value17=" + this.value17 + ")";
  }

  public T17 getValue17() {
    return this.value17;
  }
}
