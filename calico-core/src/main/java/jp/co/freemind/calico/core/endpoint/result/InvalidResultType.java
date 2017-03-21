package jp.co.freemind.calico.core.endpoint.result;

import javax.annotation.Nonnull;

public class InvalidResultType implements ResultType {
  private final ResultType resultType;

  public InvalidResultType(@Nonnull ResultType resultType) {
    this.resultType = resultType;
  }

  @Override
  public int getStatus() {
    return 400;
  }

  @Override
  public String getMimeType() {
    return this.resultType.getMimeType();
  }
}
