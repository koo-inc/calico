package jp.co.freemind.calico.core.util.type;

public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
  private final T3 value3;

  public Tuple3(T1 value1, T2 value2, T3 value3) {
    super(value1, value2);
    this.value3 = value3;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Tuple3)) return false;
    final Tuple3 other = (Tuple3) o;
    if (!other.canEqual((Object) this)) return false;
    if (!super.equals(o)) return false;
    final Object this$value3 = this.value3;
    final Object other$value3 = other.value3;
    if (this$value3 == null ? other$value3 != null : !this$value3.equals(other$value3)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + super.hashCode();
    final Object $value3 = this.value3;
    result = result * PRIME + ($value3 == null ? 43 : $value3.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Tuple3;
  }

  public String toString() {
    return "Tuple3(super=" + super.toString() + ", value3=" + this.value3 + ")";
  }

  public T3 getValue3() {
    return this.value3;
  }
}
