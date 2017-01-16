package calicosample.core;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import jp.co.freemind.calico.util.NetworkUtil;
import org.seasar.doma.jdbc.Config;
import com.google.common.base.Strings;
import com.google.inject.servlet.RequestScoped;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.dao.UserInfoDao;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.auth.AuthToken;
import jp.co.freemind.calico.context.Context;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by tasuku on 15/04/20.
 */
@RequestScoped
public class WebContext implements Context<CalicoSampleAuthInfo> {
  public static final String SESSION_TOKEN_COOKIE = "SESSIONID";

  @Getter private AuthToken authToken;
  private CalicoSampleAuthInfo authInfo;
  @Getter private String remoteAddress;

  @Inject Config config;
  @Inject UserInfoDao userInfoDao;

  @Inject
  public WebContext(HttpServletRequest request) {
    authToken = getAuthToken(request);
    remoteAddress = NetworkUtil.getRemoteAddrWithConsiderForwarded(request);
  }

  @Override
  public CalicoSampleAuthInfo getAuthInfo() {
    if (authInfo != null) {
      return authInfo;
    }

    if (!Strings.isNullOrEmpty(authToken.getId())) {
      Optional<UserInfo> userInfo = getUserInfo(Integer.parseInt(authToken.getId()));
      authInfo = userInfo.map(CalicoSampleAuthInfo::of).orElseGet(CalicoSampleAuthInfo::ofNull);
    }
    else {
      authInfo = CalicoSampleAuthInfo.ofNull();
    }

    return authInfo;
  }

  private LocalDateTime processDateTime = LocalDateTime.now();

  @Override
  public LocalDateTime getProcessDateTime() {
    return processDateTime;
  }

  public void reset(@NonNull CalicoSampleAuthInfo authInfo) {
    this.authInfo = authInfo;
    if (authInfo.isAuthenticated()) {
      authToken = AuthToken.create(String.valueOf(authInfo.getUserId()), authToken.getRemoteAddress(), getProcessDateTime());
    }
    else {
      authToken = AuthToken.createEmpty(authToken.getRemoteAddress(), getProcessDateTime());
    }
  }

  private Optional<UserInfo> getUserInfo(Integer id) {
    return Optional.ofNullable(config.getTransactionManager().required(() -> userInfoDao.findById(id)));
  }

  private AuthToken getAuthToken(HttpServletRequest request) {
    return getAuthTokenValue(request)
      .flatMap(this::getAuthToken)
      .orElseGet(() -> AuthToken.createEmpty(NetworkUtil.getRemoteAddrWithConsiderForwarded(request), getProcessDateTime()));
  }

  private Optional<AuthToken> getAuthToken(String authTokenValue) {
    try {
      return Optional.of(AuthToken.of(authTokenValue));
    }
    catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private Optional<String> getAuthTokenValue(HttpServletRequest request) {
    if (request.getCookies() == null) return Optional.empty();
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals(SESSION_TOKEN_COOKIE)) {
        return Optional.ofNullable(cookie.getValue());
      }
    }
    return Optional.empty();
  }
}
