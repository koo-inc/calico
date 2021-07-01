package jp.co.freemind.calico.core.endpoint.validation

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Function


class ValidatorTest extends Specification {
  @Shared
  def validator = Validator.factory()
    .when {v -> true}
      .then({ f, v -> v != null },
        { v -> new Message("必須項目です。") } as Function)
    .when({v -> v.is(Father) || v.is(Mother)})
      .thenNesting()
    .getValidator()

  def "Validate"() {
    given:
    def form = new Form(
      kname1: null,
      kname2: null,
      children: [ new Child(), new Child() ],
      friends: [
        foo: new Friend(),
        bar: new Friend(
          children: [ new Child(), new Child() ]
        ),
      ],
      father: new Father(),
      mother: null
    )

    when:
    def violations = validator.validate(form.class, form)

    then:
    assert violations.toMap() == [
      "kname1":["必須項目です。"],
      "kname2":["必須項目です。"],
      "children.0.kname1":["必須項目です。"],
      "children.0.kname2":["必須項目です。"],
      "children.1.kname1":["必須項目です。"],
      "children.1.kname2":["必須項目です。"],
      "friends.foo.kname1":["必須項目です。"],
      "friends.foo.kname2":["必須項目です。"],
      "friends.foo.children":["必須項目です。"],
      "friends.bar.kname1":["必須項目です。"],
      "friends.bar.kname2":["必須項目です。"],
      "friends.bar.children.0.kname1":["必須項目です。"],
      "friends.bar.children.0.kname2":["必須項目です。"],
      "friends.bar.children.1.kname1":["必須項目です。"],
      "friends.bar.children.1.kname2":["必須項目です。"],
      "father.kname1":["必須項目です。"],
      "father.kname2":["必須項目です。"],
      "mother":["必須項目です。"],
    ]
  }
};

class Form {
  String kname1
  String kname2
  List<Child> children
  Map<String, Friend> friends
  Father father
  Mother mother
};
class Child {
  String kname1
  String kname2
};
class Friend {
  String kname1
  String kname2
  List<Child> children
};
class Father {
  String kname1
  String kname2
};
class Mother {
  String kname1
  String kname2
};
