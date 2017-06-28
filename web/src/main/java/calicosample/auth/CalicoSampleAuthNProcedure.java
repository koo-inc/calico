package calicosample.auth;

import javax.annotation.Nonnull;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.AuthenticationProcedure;

@SuppressWarnings("unchecked")
public class CalicoSampleAuthNProcedure implements AuthenticationProcedure {
  @Nonnull
  @Override
  public CalicoSampleAuthInfo proceed(@Nonnull AuthToken authToken) {
    if (authToken.getId().isEmpty()) return CalicoSampleAuthInfo.ofNull(authToken.getRemoteAddress(), authToken.getCreatedAt());
    return CalicoSampleAuthInfo.of(authToken, token ->
      Zone.getCurrent().getInstance(UserInfoDao.class).findByLoginId(token.getId()));
  }
}
