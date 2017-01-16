package jp.co.freemind.calico.core.util

import jp.co.freemind.calico.core.util.function.TriConsumer
import spock.lang.Specification

import java.util.function.Consumer
/**
 * Created by kakusuke on 15/06/10.
 */
class OptionalUtilTest extends Specification {
  def "coalesce は、最初に出現するnullでない値か、すべてnullの場合はnullを返すこと"() {
    given:
    def list = [Optional.ofNullable(first), Optional.ofNullable(second), Optional.ofNullable(third)]

    when:
    def value = OptionalUtil.coalesce(list)

    then:
    assert value == Optional.ofNullable(expected)

    where:
    first | second | third || expected
    1     | null   | null  || 1
    null  | 1      | null  || 1
    null  | null   | 1     || 1
    1     | 2      | 3     || 1
    null  | null   | null  || null
  }

  def "ifPresentAll はすべての要素が存在する場合に処理を実行すること"() {
    when:
    def args = null
    OptionalUtil.ifPresentAll(Optional.ofNullable(first), Optional.ofNullable(second), Optional.ofNullable(third), new TriConsumer<Integer, Integer, Integer>() {
      @Override void accept(Integer firstVal, Integer secondVal, Integer thirdVal) {
        args = [firstVal, secondVal, thirdVal]
      }
    })

    then:
    assert args == expected

    where:
    first | second | third || expected
    1     | 2      | 3     || [1, 2, 3]
    null  | 2      | 3     || null
    1     | null   | 3     || null
    1     | 2      | null  || null
  }

  def "ifPresentAll (List形式) はすべての要素が存在する場合に処理を実行すること"() {
    when:
    def args = null
    OptionalUtil.ifPresentAll([Optional.ofNullable(first), Optional.ofNullable(second), Optional.ofNullable(third)], new Consumer<List<Integer>>() {
      @Override void accept(List<Integer> values) {
        args = values
      }
    })

    then:
    assert args == expected

    where:
    first | second | third || expected
    1     | 2      | 3     || [1, 2, 3]
    null  | 2      | 3     || null
    1     | null   | 3     || null
    1     | 2      | null  || null
  }

  def "ifAbsentAll はすべての要素が存在しない場合に処理を実行すること"() {
    when:
    def called = false
    OptionalUtil.ifAbsentAll(Optional.ofNullable(first), Optional.ofNullable(second), Optional.ofNullable(third), new Runnable() {
      @Override void run() {
        called = true
      }
    })

    then:
    assert called == expected

    where:
    first | second | third || expected
    1     | 2      | 3     || false
    null  | 2      | 3     || false
    1     | null   | 3     || false
    1     | 2      | null  || false
    null  | null   | null  || true
  }

  def "ifAbsentAll (List形式) はすべての要素が存在しない場合に処理を実行すること"() {
    when:
    def called = false
    OptionalUtil.ifAbsentAll([Optional.ofNullable(first), Optional.ofNullable(second), Optional.ofNullable(third)], new Runnable() {
      @Override void run() {
        called = true
      }
    })

    then:
    assert called == expected

    where:
    first | second | third || expected
    1     | 2      | 3     || false
    null  | 2      | 3     || false
    1     | null   | 3     || false
    1     | 2      | null  || false
    null  | null   | null  || true
  }
}
