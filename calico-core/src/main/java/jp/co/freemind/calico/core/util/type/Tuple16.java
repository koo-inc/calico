package jp.co.freemind.calico.core.util.type;

public class Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> extends Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
  private final T16 value16;

  public Tuple16(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15);
    this.value16 = value16;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple16)) return false;
    final Tuple16 other = (Tuple16) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value16 = this.value16;
    final Object other$value16 = other.value16;
    if (this$value16 == null ? other$value16 != null : !this$value16.equals(other$value16)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value16 = this.value16;
    result = result * PRIME + ($value16 == null ? 43 : $value16.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple16;
  }

  public String toString() {
    return "Tuple16(super=" + super.toString() + ", value16=" + this.value16 + ")";
  }

  public T16 getValue16() {
    return this.value16;
  }
}
