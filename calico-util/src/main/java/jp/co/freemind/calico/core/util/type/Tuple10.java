package jp.co.freemind.calico.core.util.type;

public class Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
  private final T10 value10;

  public Tuple10(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10) {
    super(value1, value2, value3, value4, value5, value6, value7, value8, value9);
    this.value10 = value10;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple10)) return false;
    final Tuple10 other = (Tuple10) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value10 = this.value10;
    final Object other$value10 = other.value10;
    if (this$value10 == null ? other$value10 != null : !this$value10.equals(other$value10)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value10 = this.value10;
    result = result * PRIME + ($value10 == null ? 43 : $value10.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple10;
  }

  public String toString() {
    return "Tuple10(super=" + super.toString() + ", value10=" + this.value10 + ")";
  }

  public T10 getValue10() {
    return this.value10;
  }
}
