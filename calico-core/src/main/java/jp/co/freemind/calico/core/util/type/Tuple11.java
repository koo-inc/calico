package jp.co.freemind.calico.core.util.type;

public class Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extends Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
  private final T11 value11;

  public Tuple11(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
    this.value11 = value11;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple11)) return false;
    final Tuple11 other = (Tuple11) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value11 = this.value11;
    final Object other$value11 = other.value11;
    if (this$value11 == null ? other$value11 != null : !this$value11.equals(other$value11)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value11 = this.value11;
    result = result * PRIME + ($value11 == null ? 43 : $value11.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple11;
  }

  public String toString() {
    return "Tuple11(super=" + super.toString() + ", value11=" + this.value11 + ")";
  }

  public T11 getValue11() {
    return this.value11;
  }
}
