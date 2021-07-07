package jp.co.freemind.calico.core.util.type;

public class Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extends Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
  private final T14 value14;

  public Tuple14(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13);
    this.value14 = value14;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple14)) return false;
    final Tuple14 other = (Tuple14) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value14 = this.value14;
    final Object other$value14 = other.value14;
    if (this$value14 == null ? other$value14 != null : !this$value14.equals(other$value14)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value14 = this.value14;
    result = result * PRIME + ($value14 == null ? 43 : $value14.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple14;
  }

  public String toString() {
    return "Tuple14(super=" + super.toString() + ", value14=" + this.value14 + ")";
  }

  public T14 getValue14() {
    return this.value14;
  }
}
