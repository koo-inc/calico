package jp.co.freemind.calico.servlet;

import javax.annotation.Nonnull;

import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;

public interface AuthenticationProcedure {
  @Nonnull
  <T extends AuthInfo> T proceed(@Nonnull AuthToken authToken);
}
