package jp.co.freemind.calico.core.util.type;

public class Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> extends Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
  private final T21 value21;

  public Tuple21(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20);
    this.value21 = value21;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple21)) return false;
    final Tuple21 other = (Tuple21) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value21 = this.value21;
    final Object other$value21 = other.value21;
    if (this$value21 == null ? other$value21 != null : !this$value21.equals(other$value21)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value21 = this.value21;
    result = result * PRIME + ($value21 == null ? 43 : $value21.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple21;
  }

  public String toString() {
    return "Tuple21(super=" + super.toString() + ", value21=" + this.value21 + ")";
  }

  public T21 getValue21() {
    return this.value21;
  }
}
