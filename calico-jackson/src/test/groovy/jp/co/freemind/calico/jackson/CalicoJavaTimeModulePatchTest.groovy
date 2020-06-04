package jp.co.freemind.calico.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import jp.co.freemind.calico.jackson.ObjectMapperProvider
import jp.co.freemind.calico.jackson.deser.LocalDateDeserializer
import jp.co.freemind.calico.jackson.deser.LocalDateTimeDeserializer
import jp.co.freemind.calico.jackson.deser.LocalTimeDeserializer
import jp.co.freemind.calico.jackson.ser.LocalDateSerializer
import jp.co.freemind.calico.jackson.ser.LocalDateTimeSerializer
import jp.co.freemind.calico.jackson.ser.LocalTimeSerializer
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
/**
 * Created by kakusuke on 15/07/22.
 */
class CalicoJavaTimeModulePatchTest extends Specification {

  @Shared
  ObjectMapper mapper

  @Shared
  ObjectMapper prettyMapper

  def setup() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"))
    mapper = ObjectMapperProvider.createMapper()
    prettyMapper = ObjectMapperProvider.createMapper(new SimpleModule() {{
      addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("uuuu/M/d H:m:s")));
      addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy/M/d H:m:s")));

      addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy/M/d")));
      addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy/M/d")));

      addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("H:m:s")));
      addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("H:m:s")));
    }})
  }

  def "check timezone"() {
    expect:
    assert TimeZone.getDefault().getDisplayName(Locale.JAPANESE) == "日本標準時"
    and:
    assert TimeZone.getDefault().toZoneId().getId() == "Asia/Tokyo"
    and:
    ZoneId.systemDefault().getId() == "Asia/Tokyo"
  }

  def "de/serialize LocalDateTime"() {
    when:
    def str = '"2012-05-02T17:15:00Z"'
    def translated = mapper.readValue(str, LocalDateTime)
    then:
    assert translated == LocalDateTime.of(2012, 05, 03, 02, 15, 00)
    and:
    assert str == mapper.writeValueAsString(translated)
  }

  def "de/serialize LocalDate"() {
    when:
    def str = '"2012-05-02T15:00:00Z"'
    def translated = mapper.readValue(str, LocalDate)
    then:
    assert translated == LocalDate.of(2012, 05, 03)
    and:
    assert str == mapper.writeValueAsString(translated)
  }

  def "de/serialize LocalTime"() {
    when:
    def str = '"1969-12-31T17:55:22Z"'
    def translated = mapper.readValue(str, LocalTime)
    then:
    assert translated == LocalTime.of(2, 55, 22)
    and:
    assert str == mapper.writeValueAsString(translated)
  }

  def "de/serialize LocalDateTime with pretty print"() {
    when:
    def str = '"2012/5/2 17:15:0"'
    def translated = prettyMapper.readValue(str, LocalDateTime)
    then:
    assert translated == LocalDateTime.of(2012, 5, 2, 17, 15, 00)
    and:
    assert str == prettyMapper.writeValueAsString(translated)
  }

  def "de/serialize LocalDate with pretty print"() {
    when:
    def str = '"2012/5/2"'
    def translated = prettyMapper.readValue(str, LocalDate)
    then:
    assert translated == LocalDate.of(2012, 5, 2)
    and:
    assert str == prettyMapper.writeValueAsString(translated)
  }

  def "de/serialize LocalTime with pretty print"() {
    when:
    def str = '"17:5:22"'
    def translated = prettyMapper.readValue(str, LocalTime)
    then:
    assert translated == LocalTime.of(17, 5, 22)
    and:
    assert str == prettyMapper.writeValueAsString(translated)
  }

}
