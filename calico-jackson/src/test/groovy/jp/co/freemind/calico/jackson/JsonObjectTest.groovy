package jp.co.freemind.calico.jackson

import groovy.transform.EqualsAndHashCode
import jp.co.freemind.calico.core.extenum.ExtEnum
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
/**
 * Created by kakusuke on 15/08/05.
 */
class JsonObjectTest extends Specification {
  def "オブジェクトをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<Hoge>('{"id":1,"text":"a","hoge_flg":true}') {}

    then:
    assert obj.get() == new Hoge(id: 1, text: "a", hogeFlg: true)
    assert obj.toString() == '{"id":1,"text":"a","hoge_flg":true}'
  }

  def "オブジェクトのリストをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<List<Hoge>>('[{"id":1,"text":"a","hoge_flg":true}]') {}

    then:
    assert obj.get() == [new Hoge(id: 1, text: "a", hogeFlg: true)]
    assert obj.toString() == '[{"id":1,"text":"a","hoge_flg":true}]'
  }

  def "Integerをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<Integer>('1') {}

    then:
    assert obj.get() == 1
    assert obj.toString() == '1'
  }

  def "Integerのリストをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<List<Integer>>('[1,2]') {}

    then:
    assert obj.get() == [1, 2]
    assert obj.toString() == '[1,2]'
  }

  def "Java8のオブジェクトをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<Fuga>(data) {}

    then:
    assert obj.get() == new Fuga(
      id: Optional.ofNullable(id),
      at: LocalDateTime.parse(at),
      today: LocalDate.parse(today),
      now: LocalTime.parse(now)
    )
    assert obj.toString() == data

    where:
    data                                                                                   | id   | at                        | today        | now
    '{"id":null,"at":"2014-01-01T00:11:22.333","today":"2014-01-02","now":"13:12:12.112"}' | null | "2014-01-01T00:11:22.333" | "2014-01-02" | "13:12:12.112"
  }

  def "Enum入りオブジェクトをデ／シリアライズできること"() {
    when:
    def obj = new JsonObject<Piyo>('{"family_type":1}') {}

    then:
    assert obj.get() == new Piyo(familyType: FamilyType.PARENT)
    assert obj.toString() == '{"family_type":{"id":1,"name":"親","NAME":"PARENT"}}'
  }

  def "拡張クラスをフィールドとしてデ／シリアライズできること"() {
    given:
    def mapper = ObjectMapperProvider.createMapper()
    when:
    def obj = mapper.readValue('{"intList":[1,2,3],"textList":["a","b","c"]}', Foo)

    then:
    assert obj == new Foo(intList: [1, 2, 3], textList: ["a", "b", "c"])
    assert mapper.writeValueAsString(obj) == '{"intList":[1,2,3],"textList":["a","b","c"]}'
  }


  @EqualsAndHashCode
  static class Hoge {
    Integer id
    String text
    boolean hogeFlg
  }

  @EqualsAndHashCode
  static class Fuga {
    Optional<Integer> id
    LocalDateTime at
    LocalDate today
    LocalTime now;
  }

  @EqualsAndHashCode
  static class Piyo {
    FamilyType familyType
  }

  @EqualsAndHashCode
  static class Foo {
    IntList intList
    TextList textList
  }


  @EqualsAndHashCode
  static class IntList extends JsonObject<List<Integer>> {
    IntList(List<Integer> object) { super(object); }
    IntList(String json) { super(json); }
  }

  @EqualsAndHashCode
  static class TextList extends JsonObject<List<String>> {
    TextList(List<String> object) { super(object); }
    TextList(String json) { super(json); }
  }

  public enum FamilyType implements ExtEnum<Integer> {
    PARENT(1, "親")
    private final Integer id
    private final String name
    FamilyType(Integer id, String name) { this.id = id; this.name = name }
    public static FamilyType of(Integer id){
      return of(id, FamilyType.class)
    }
    @Override Integer getId() { return id }
    @Override String getName() { return name }
    public Integer getValue() { return id }
  }
}
