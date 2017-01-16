package helper

import calicosample.core.auth.CalicoSampleAuthInfo
import jp.co.freemind.calico.context.Context
import jp.co.freemind.calico.guice.InjectUtil

import java.time.LocalDate
import java.time.LocalDateTime

public class ContextHelper {
  static void setAuthInfo(CalicoSampleAuthInfo authInfo) {
    context.switchTo(Context.builder()
      .inheritFrom(context)
      .authInfo(authInfo)
      .build());
  }

  static void setProcessDate(LocalDate processDate) {
    setProcessDateTime(processDate.atStartOfDay())
  }

  static void setProcessDateTime(LocalDateTime processDateTime) {
    context.switchTo(Context.builder()
      .inheritFrom(context)
      .processDateTime(processDateTime)
      .build());
  }

  static Context getContext() {
    return InjectUtil.getInstance(Context);
  }

  static void reset() {
    while(context.contextStack.size() > 1) {
      context.contextStack.pop()
    }
  }
}
