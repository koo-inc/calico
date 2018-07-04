package jp.co.freemind.calico.core.util.type;

public class Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> extends Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
  private final T19 value19;

  public Tuple19(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18);
    this.value19 = value19;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple19)) return false;
    final Tuple19 other = (Tuple19) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value19 = this.value19;
    final Object other$value19 = other.value19;
    if (this$value19 == null ? other$value19 != null : !this$value19.equals(other$value19)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value19 = this.value19;
    result = result * PRIME + ($value19 == null ? 43 : $value19.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple19;
  }

  public String toString() {
    return "Tuple19(super=" + super.toString() + ", value19=" + this.value19 + ")";
  }

  public T19 getValue19() {
    return this.value19;
  }
}
