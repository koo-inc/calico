package calicosample.service;

import java.util.Optional;
import javax.inject.Inject;
import javax.validation.Valid;

import jp.co.freemind.calico.context.Context;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.dao.UserInfoDao;
import calicosample.dto.form.auth.LoginForm;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.auth.AuthToken;
import jp.co.freemind.calico.service.Service;
import jp.co.freemind.calico.service.exception.ServiceException;

public class AuthService extends Service {
  @Inject UserInfoDao userInfoDao;
  @Inject
  Context<CalicoSampleAuthInfo> context;

  public CalicoSampleAuthInfo login(@Valid LoginForm form) {
    Optional<UserInfo> userInfo = userInfoDao.findForLogin(form.getLoginId(), form.getPassword());
    CalicoSampleAuthInfo authInfo = CalicoSampleAuthInfo.of(userInfo.orElseThrow(() ->
      new ServiceException("loginId", "IDまたはパスワードが間違っています。")));
    Context.Builder<CalicoSampleAuthInfo> builder = Context.builder();
    context.switchTo(builder
      .inheritFrom(context)
      .authInfo(authInfo)
      .authToken(AuthToken.create(String.valueOf(authInfo.getUserId()), context.getRemoteAddress(), context.getProcessDateTime()))
      .build());
    return authInfo;
  }

  public void logout() {
    Context.Builder<CalicoSampleAuthInfo> builder = Context.builder();
    context.switchTo(builder
      .inheritFrom(context)
      .authInfo(CalicoSampleAuthInfo.ofNull())
      .authToken(AuthToken.createEmpty(context.getRemoteAddress(), context.getProcessDateTime()))
      .build());
  }
}
