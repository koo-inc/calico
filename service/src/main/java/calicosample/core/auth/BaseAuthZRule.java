package calicosample.core.auth;

import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;

import calicosample.Messages;
import calicosample.extenum.CalicoSampleAuthority;
import jp.co.freemind.calico.core.auth.AuthorizationRule;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.util.Types;
import jp.co.freemind.calico.core.di.Context;

public class BaseAuthZRule implements AuthorizationRule {
  @Override
  public void apply(Context context, Class<? extends Endpoint<?, ?>> endpointClass, Object input) throws AuthorizationException {
    if (getAnnotationStream(endpointClass, AllowAll.class).findAny().isPresent()) return;

    if (!isAuthenticated(context)) {
      throw new AuthorizationException(Messages.NOT_AUTHORIZED);
    }

    List<CalicoSampleAuthority[]> authoritiesList = getAnnotationStream(endpointClass, AllowOnly.class)
      .map(AllowOnly::value)
      .collect(toList());

    for (CalicoSampleAuthority[] authorities : authoritiesList) {
      if (!isAuthorizedWith(context, authorities)) {
        throw new AuthorizationException(Messages.NOT_AUTHORIZED);
      }
    }
  }

  private <A extends Annotation> Stream<A> getAnnotationStream(Class<?> type, Class<A> annotation) {
    return Stream.concat(Stream.of(type), Types.getAncestorPackages(type).stream())
      .filter(e -> e.isAnnotationPresent(annotation))
      .map(e -> e.getAnnotation(annotation));
  }

  private boolean isAuthenticated(Context context) {
    return context.getAuthInfo().isAuthenticated();
  }
}
