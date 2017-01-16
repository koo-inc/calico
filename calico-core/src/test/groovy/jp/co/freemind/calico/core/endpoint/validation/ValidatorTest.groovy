package jp.co.freemind.calico.core.endpoint.validation

import com.fasterxml.classmate.TypeResolver
import spock.lang.Specification


class ValidatorTest extends Specification {
  def "validationTest"() {
    when:
    def resolver = new TypeResolver()

    then:
    assert resolver.resolve(Child).typeParametersFor(Parent) == [resolver.resolve(String)]

    then:
    assert resolver.resolve(GrandChild).typeParametersFor(Parent) == [resolver.resolve(String)]

    then:
    assert resolver.resolve(Parent).getMemberFields().get(0).getDeclaringType().typeParametersFor(GrandChild) == [resolver.resolve(String)]
  }

  static class Parent<T> {
    public T hoge
  }

  static class Child extends Parent<String> {}

  static class GrandChild extends Child {
    public Boolean test
  }
}
