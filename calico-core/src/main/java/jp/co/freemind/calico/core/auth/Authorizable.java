package jp.co.freemind.calico.core.auth;

import java.util.Arrays;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Context;

@FunctionalInterface
public interface Authorizable {
  void authorize(Context context, EndpointInvocation invocation) throws AuthorizationException;

  default boolean hasRights(Context context, Rights... rights) {
    return context.getAuthInfo()
      .flatMap(ai -> Arrays.stream(rights)
        .filter(ai::hasRights)
        .findFirst())
      .isPresent();
  }
}
