package jp.co.freemind.calico.core.endpoint.interceptor;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jp.co.freemind.calico.core.auth.AuthorizationRule;
import jp.co.freemind.calico.core.auth.Restriction;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.EndpointRoot;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.calico.core.zone.Zone;

public class AuthorizationInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    authorizableStream(invocation)
      .forEach(a -> a.apply(Zone.getContext(), invocation.getEndpointClass(), invocation.getInput()));

    return invocation.proceed();
  }

  private Stream<AuthorizationRule> authorizableStream(EndpointInvocation invocation){
    Class<? extends Endpoint<?, ?>> endpointClass = invocation.getEndpointClass();

    return Stream.concat(
        Stream.of(getRestriction(endpointClass)),
        packageStream(endpointClass.getName()).map(this::getRestriction)
      )
      .filter(Optional::isPresent)
      .map(Optional::get)
      .map(Restriction::value)
      .flatMap(Arrays::stream)
      .map(c -> {
        try {
          return c.newInstance();
        } catch (ReflectiveOperationException e) {
          throw Throwables.sneakyThrow(e);
        }
      });
  }

  private Stream<Package> packageStream(String className) {
    List<Package> packages = new ArrayList<>();
    String[] fragments = className.split("\\.");
    for (int n = fragments.length - 1; n > 0; n--) {
      String pkgName = stream(copyOf(fragments, n)).collect(joining("."));
      Package pkg = Package.getPackage(pkgName);
      if (pkg == null) continue;

      packages.add(pkg);
      if (pkg.isAnnotationPresent(EndpointRoot.class)) {
        break;
      }
    }
    return packages.stream();
  }

  private Optional<Restriction> getRestriction(AnnotatedElement element) {
    return Optional.ofNullable(element.getAnnotation(Restriction.class));
  }

}
