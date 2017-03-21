package jp.co.freemind.calico.core.auth;

import java.util.Arrays;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Context;

@FunctionalInterface
public interface Authorizable {
  void authorize(Context context, EndpointInvocation invocation) throws AuthorizationException;

  default boolean hasRights(Context context, Right... rights) {
    return context.getAuthInfo()
      .map(AuthInfo::getRights)
      .flatMap(rs -> Arrays.stream(rights)
        .filter(rs::contains)
        .findFirst())
      .isPresent();
  }
}
