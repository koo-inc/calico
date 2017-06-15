package jp.co.freemind.calico.core.orm

import spock.lang.Specification


class IdentifierTest extends Specification {
  def "test getValueClass"() {
    given:
    def id = new IntegerIdentifier(1)

    expect:
    assert id.getValueClass() == Integer
  }
}

class IntegerIdentifier extends Identifier<Integer> {
  IntegerIdentifier(Integer value) {
    super(value)
  }
}
