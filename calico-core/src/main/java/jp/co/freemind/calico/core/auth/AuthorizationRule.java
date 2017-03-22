package jp.co.freemind.calico.core.auth;

import java.util.Arrays;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Context;

public interface AuthorizationRule {
  void apply(Context context, Class<? extends Endpoint<?, ?>> endpointClass, Object input) throws AuthorizationException;

  default boolean isAuthorizedWith(Context context, Authority... authorities) {
    return context.getAuthInfo()
      .map(AuthInfo::getAuthorities)
      .flatMap(rs -> Arrays.stream(authorities)
        .filter(rs::contains)
        .findFirst())
      .isPresent();
  }

}
