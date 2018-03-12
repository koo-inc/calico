package jp.co.freemind.calico.core.time

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime


class TimePointTest extends Specification {

  @Unroll
  def "#time をパースできること"() {
    expect:
    assert TimePoint.of(time).toString() == expected

    where:
    time       || expected
    '00:00'    || '00:00'
    '23:59'    || '23:59'
    '24:00'    || '24:00'
    '99:59'    || '99:59'
    '00:00:00' || '00:00'
    '23:59:59' || '23:59'
  }

  @Unroll
  def "#time をパースできないこと"() {
    expect:
    assert TimePoint.of(time) == null

    where:
    time << [
      '-0:00',
      '23:60',
      '100:00',
      '23:59:60'
    ]
  }

  @Unroll
  def "LocalTime(#hour時#min分) を変換できること"() {
    expect:
    assert TimePoint.of(LocalTime.of(hour, min)).toString() == expect
    where:
    hour | min || expect
    0    | 0   || '00:00'
    23   | 59  || '23:59'
  }

  @Unroll
  def "#hour時#min分 を変換できること"() {
    expect:
    assert TimePoint.of(hour, min).toString() == expect

    where:
    hour | min || expect
    0    | 0   || '00:00'
    23   | 59  || '23:59'
  }

  @Unroll
  def "#hour時#min分 を変換できないこと"() {
    when:
    TimePoint.of(hour, min)

    then:
    thrown(expect)

    where:
    hour | min || expect
    -1   | 0   || IllegalArgumentException
    0    | -1  || IllegalArgumentException
    100  | 0   || IllegalArgumentException
  }

  @Unroll
  def "#value分 を変換すると #expect になること"() {
    expect:
    assert TimePoint.of(value).toString() == expect
    assert TimePoint.of(value).getMinutesAmount() == value

    where:
    value || expect
    0     || '00:00'
    59    || '00:59'
    60    || '01:00'
    5999  || '99:59'
  }

  @Unroll
  def "#value分 を変換できないこと"() {
    when:
    TimePoint.of(value)

    then:
    thrown(expect)

    where:
    value || expect
    -1    || IllegalArgumentException
    6000  || IllegalArgumentException
  }
}
