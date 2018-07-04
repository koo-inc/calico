package jp.co.freemind.calico.core.util.type;

public class Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
  private final T13 value13;

  public Tuple13(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
    this.value13 = value13;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple13)) return false;
    final Tuple13 other = (Tuple13) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value13 = this.value13;
    final Object other$value13 = other.value13;
    if (this$value13 == null ? other$value13 != null : !this$value13.equals(other$value13)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value13 = this.value13;
    result = result * PRIME + ($value13 == null ? 43 : $value13.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple13;
  }

  public String toString() {
    return "Tuple13(super=" + super.toString() + ", value13=" + this.value13 + ")";
  }

  public T13 getValue13() {
    return this.value13;
  }
}
