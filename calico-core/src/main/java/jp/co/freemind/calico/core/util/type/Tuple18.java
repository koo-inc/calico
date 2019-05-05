package jp.co.freemind.calico.core.util.type;

public class Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> extends Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
  private final T18 value18;

  public Tuple18(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15, T16 value16, T17 value17, T18 value18) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17);
    this.value18 = value18;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple18)) return false;
    final Tuple18 other = (Tuple18) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value18 = this.value18;
    final Object other$value18 = other.value18;
    if (this$value18 == null ? other$value18 != null : !this$value18.equals(other$value18)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value18 = this.value18;
    result = result * PRIME + ($value18 == null ? 43 : $value18.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple18;
  }

  public String toString() {
    return "Tuple18(super=" + super.toString() + ", value18=" + this.value18 + ")";
  }

  public T18 getValue18() {
    return this.value18;
  }
}
