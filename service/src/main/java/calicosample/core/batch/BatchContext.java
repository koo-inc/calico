package calicosample.core.batch;

import java.time.LocalDateTime;

import jp.co.freemind.calico.context.Context;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.auth.AuthToken;

/**
 * Created by kakusuke on 15/07/16.
 */
public class BatchContext implements Context<CalicoSampleAuthInfo> {
  private final Context<CalicoSampleAuthInfo> context;
  private final Class<? extends Batch> batchClass;
  private LocalDateTime processDateTime;

  public BatchContext(Context<CalicoSampleAuthInfo> context, Class<? extends Batch> batchClass, LocalDateTime processDateTime) {
    this.context = context;
    this.batchClass = batchClass;
    this.processDateTime = processDateTime;
  }

  @Override
  public AuthToken getAuthToken() {
    return context.getAuthToken();
  }

  private CalicoSampleAuthInfo authInfo = CalicoSampleAuthInfo.of(UserInfo.BATCH_USER);
  @Override
  public CalicoSampleAuthInfo getAuthInfo() {
    return authInfo;
  }

  @Override
  public LocalDateTime getProcessDateTime() {
    return processDateTime;
  }

  @Override
  public String getRemoteAddress() {
    return "localhost";
  }

  @Override
  public void switchTo(Context<CalicoSampleAuthInfo> context, Runnable runnable) {
    this.context.switchTo(context, runnable);
  }

  public Class<? extends Batch> getBatchClass() {
    return this.batchClass;
  }
}
