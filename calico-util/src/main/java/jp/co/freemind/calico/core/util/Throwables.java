package jp.co.freemind.calico.core.util;

import javax.annotation.Nonnull;

public class Throwables {
  public static RuntimeException sneakyThrow(Throwable t) {
    if (t == null) return new NullPointerException("t");
    sneakyThrow0(t);
    return new RuntimeException(t);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void sneakyThrow0(@Nonnull Throwable t) throws T {
    throw (T) t;
  }
}
