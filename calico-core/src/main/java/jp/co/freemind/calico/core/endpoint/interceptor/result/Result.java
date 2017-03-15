package jp.co.freemind.calico.core.endpoint.interceptor.result;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jp.co.freemind.calico.core.zone.Context;

public class Result {
  private final ResultType resultType;
  private final Context context;
  private final Object output;

  public Result(ResultType resultType, @Nonnull Context context, @Nullable Object output) {
    this.resultType = resultType;
    this.context = context;
    this.output = output;
  }

  public ResultType getResultType() {
    return resultType;
  }

  public Optional<Object> getOutput() {
    return Optional.ofNullable(output);
  }

  public Context getContext() {
    return context;
  }
}
