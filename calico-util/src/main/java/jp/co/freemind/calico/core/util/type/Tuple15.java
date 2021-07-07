package jp.co.freemind.calico.core.util.type;

public class Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extends Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
  private final T15 value15;

  public Tuple15(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14);
    this.value15 = value15;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple15)) return false;
    final Tuple15 other = (Tuple15) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value15 = this.value15;
    final Object other$value15 = other.value15;
    if (this$value15 == null ? other$value15 != null : !this$value15.equals(other$value15)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value15 = this.value15;
    result = result * PRIME + ($value15 == null ? 43 : $value15.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple15;
  }

  public String toString() {
    return "Tuple15(super=" + super.toString() + ", value15=" + this.value15 + ")";
  }

  public T15 getValue15() {
    return this.value15;
  }
}
