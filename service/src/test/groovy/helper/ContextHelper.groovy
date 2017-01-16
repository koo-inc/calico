package helper

import calicosample.core.auth.CalicoSampleAuthInfo
import com.google.inject.TypeLiteral
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

  static Context<CalicoSampleAuthInfo> getContext() {
    return InjectUtil.getInstance(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {});
  }

  static void reset() {
    while(context.contextStack.size() > 1) {
      context.contextStack.pop()
    }
  }
}
