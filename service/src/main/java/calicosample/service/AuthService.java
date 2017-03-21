package calicosample.service;

import javax.annotation.Nullable;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.exception.InconsistentDataException;
import jp.co.freemind.calico.core.zone.Context;

public class AuthService {
  @Inject private UserInfoDao userInfoDao;
  @Inject private Context context;

  public CalicoSampleAuthInfo login(String loginId, String password) {
    UserInfo.WithAdditionalData userInfo = userInfoDao.findForLogin(loginId, password).orElseThrow(() ->
      new InconsistentDataException("loginId", "IDまたはパスワードが間違っています。"));
    return CalicoSampleAuthInfo.of(userInfo, context.getRemoteAddress(), context.getProcessDateTime());
  }

  public CalicoSampleAuthInfo logout() {
    return CalicoSampleAuthInfo.ofNull(context.getRemoteAddress(), context.getProcessDateTime());
  }

  public CalicoSampleAuthInfo keep(@Nullable CalicoSampleAuthInfo authInfo) {
    if (authInfo == null || authInfo.getUserId() == null) return CalicoSampleAuthInfo.ofNull(context.getRemoteAddress(), context.getProcessDateTime());
    UserInfo.WithAdditionalData userInfo = userInfoDao.findById(authInfo.getUserId());
    return CalicoSampleAuthInfo.of(userInfo, context.getRemoteAddress(), context.getProcessDateTime());
  }
}
