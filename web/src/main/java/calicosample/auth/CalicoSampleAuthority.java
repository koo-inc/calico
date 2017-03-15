package calicosample.auth;

import javax.annotation.Nonnull;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.CertificateAuthority;

@SuppressWarnings("unchecked")
public class CalicoSampleAuthority implements CertificateAuthority {
  @Nonnull
  @Override
  public CalicoSampleAuthInfo authenticate(@Nonnull AuthToken authToken) {
    if (authToken.getId() == null) return CalicoSampleAuthInfo.ofNull(authToken.getRemoteAddress(), authToken.getCreatedAt());
    return CalicoSampleAuthInfo.of(authToken, token ->
      Zone.getCurrent().getInstance(UserInfoDao.class).findByLoginId(token.getId()));
  }
}
