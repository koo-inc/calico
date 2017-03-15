package jp.co.freemind.calico.core.endpoint.dto;

import jp.co.freemind.calico.core.zone.Context;

public class ContextualOutput<T> {
  private final Context context;
  private final T output;

  public ContextualOutput(Context context, T output) {
    this.context = context;
    this.output = output;
  }

  public Context getContext() {
    return context;
  }
  public T getOutput() {
    return output;
  }
}
