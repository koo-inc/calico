package jp.co.freemind.calico.core.endpoint.interceptor;

import java.lang.reflect.AnnotatedElement;
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
import jp.co.freemind.calico.core.util.Types;
import jp.co.freemind.calico.core.zone.Zone;

public class AuthorizationInterceptor implements EndpointInterceptor {
  @Override
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    ruleStream(invocation)
      .forEach(rule -> rule.apply(Zone.getContext(), invocation.getEndpointClass(), invocation.getInput()));

    return invocation.proceed();
  }

  private Stream<AuthorizationRule> ruleStream(EndpointInvocation invocation){
    Class<? extends Endpoint<?, ?>> endpointClass = invocation.getEndpointClass();

    return Stream.concat(
        Stream.of(getRestriction(endpointClass)),
        packageStream(endpointClass).map(this::getRestriction)
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

  private Stream<Package> packageStream(Class<?> type) {
    List<Package> packages = Types.getAncestorPackages(type);
    Optional<Package> rootPkg = packages.stream()
      .filter(pkg -> pkg.isAnnotationPresent(EndpointRoot.class))
      .findFirst();
    return rootPkg.map(pkg -> packages.subList(0, packages.indexOf(pkg) + 1)).orElse(packages)
      .stream();
  }

  private Optional<Restriction> getRestriction(AnnotatedElement element) {
    return Optional.ofNullable(element.getAnnotation(Restriction.class));
  }

}
