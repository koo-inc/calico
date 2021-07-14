package jp.co.freemind.calico.core.util.type;

public class Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
  private final T12 value12;

  public Tuple12(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
    this.value12 = value12;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple12)) return false;
    final Tuple12 other = (Tuple12) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value12 = this.value12;
    final Object other$value12 = other.value12;
    if (this$value12 == null ? other$value12 != null : !this$value12.equals(other$value12)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value12 = this.value12;
    result = result * PRIME + ($value12 == null ? 43 : $value12.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple12;
  }

  public String toString() {
    return "Tuple12(super=" + super.toString() + ", value12=" + this.value12 + ")";
  }

  public T12 getValue12() {
    return this.value12;
  }
}
