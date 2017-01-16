package jp.co.freemind.calico.jackson.ser
import com.fasterxml.jackson.databind.ObjectMapper
import jp.co.freemind.calico.jackson.MediaModule
import jp.co.freemind.calico.core.media.Media
import jp.co.freemind.calico.jackson.media.WithPayload
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by tasuku on 15/05/01.
 */
class MediaSerializerTest extends Specification {
  @Shared
  ObjectMapper mapper = new ObjectMapper();
  def setupSpec() {
    mapper.registerModule(new MediaModule());
  }

  def "id がある場合、id と meta をシリアライズすること"() {
    when:
    def media = new Media();
    media.id = "a/b/c"
    media.meta.name = "abc"
    media.meta.size = 10
    media.meta.type = "text/plain"
    media.payload = "abc".bytes
    def serialized = mapper.writeValueAsString(media)

    then:
    serialized == '{"id":"a/b/c","meta":{"name":"abc","type":"text/plain","size":10}}'

  }

  def "id がない場合、meta と payload をシリアライズすること"() {
    when:
    def media = new Media();
    media.meta.name = "abc"
    media.meta.size = 10
    media.meta.type = "text/plain"
    media.payload = "abc".bytes
    def serialized = mapper.writeValueAsString(media)

    then:
    serialized == '{"payload":"YWJj","meta":{"name":"abc","type":"text/plain","size":10}}'
  }

  def "WithPayload アノテーションをつけた場合、payload 付きでシリアライズすること"() {
    when:
    def media = new Media()
    media.id = "a/b/c"
    media.meta.name = "abc"
    media.meta.size = 10
    media.meta.type = "text/plain"
    media.payload = "abc".bytes
    def record = new Record(media: media)
    def serialized = mapper.writeValueAsString(record)

    then:
    serialized == '{"media":{"id":"a/b/c","payload":"YWJj","meta":{"name":"abc","type":"text/plain","size":10}}}'
  }

  class Record {
    @WithPayload
    Media media;
  }
}
