package jp.co.freemind.calico.core.util.type;

public class Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
  private final T22 value22;

  public Tuple22(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18, T19 value19, T20 value20, T21 value21, T22 value22) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20, value21);
    this.value22 = value22;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple22)) return false;
    final Tuple22 other = (Tuple22) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value22 = this.value22;
    final Object other$value22 = other.value22;
    if (this$value22 == null ? other$value22 != null : !this$value22.equals(other$value22)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value22 = this.value22;
    result = result * PRIME + ($value22 == null ? 43 : $value22.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple22;
  }

  public String toString() {
    return "Tuple22(super=" + super.toString() + ", value22=" + this.value22 + ")";
  }

  public T22 getValue22() {
    return this.value22;
  }
}
