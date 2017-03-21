package jp.co.freemind.calico.core.endpoint.result;

import javax.annotation.Nonnull;

public class AbnormalResultType implements ResultType {
  private final int status;
  private final ResultType resultType;

  AbnormalResultType(int status, @Nonnull ResultType resultType) {
    this.status = status;
    this.resultType = resultType;
  }

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getMimeType() {
    return this.resultType.getMimeType();
  }
}
