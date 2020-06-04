package jp.co.freemind.calico.jackson.deser

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Binder
import com.google.inject.Module
import helper.media.MockStorage
import jp.co.freemind.calico.core.di.InjectorManager
import jp.co.freemind.calico.core.media.Media
import jp.co.freemind.calico.core.media.MediaProxy
import jp.co.freemind.calico.core.media.MediaStorage
import jp.co.freemind.calico.jackson.MediaModule
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by tasuku on 15/05/01.
 */
class MediaDeserializerTest extends Specification {
  @Shared
  ObjectMapper mapper = new ObjectMapper()

  def setupSpec() {
    mapper.registerModule(new MediaModule())
    InjectorManager.initialize() { s ->
      s.modules(new Module() {
        @Override
        void configure(Binder binder) {
          binder.bind(MediaStorage).to(MockStorage)
        }
      })
    }
  }

  def "定形オブジェクトのデシリアライズでは、適切な値で埋められているオブジェクトが返ること"() {
    when:
    def media = mapper.readValue(createJson(name, size, type, payload), Media)
    def expected = createExpected(name, size, type, payload)

    then:
    assert media.meta.name == expected.meta.name
    and:
    assert media.meta.size == expected.meta.size
    and:
    assert media.meta.type == expected.meta.type
    and:
    assert media.payload == expected.payload


    where:
     name  | size |    type      |  payload
    'test' |  11  | 'text/plain' | 'YWFh'
      null |  11  | 'text/plain' | 'YWFh'
    'test' | null | 'text/plain' | 'YWFh'
    'test' |  11  |    null      | 'YWFh'
    'test' |  11  | 'text/plain' |  null
  }

  def "id が入っていた場合、Proxy オブジェクトが返ること"() {
    when:
    def media = mapper.readValue('{"id":"a/b/c"}', Media)

    then:
    assert media instanceof MediaProxy
  }

  def "null のデシリアライズでは null が返ること"() {
    when:
    def media = mapper.readValue("null", Media)

    then:
    assert media == null
  }
  def "日本語のデシリアライズができること"() {
    when:
    def media = mapper.readValue(createJson("あああ", 6, "text/plain", "44GC44GC44GC"), Media)

    then:
    assert media.payload == "あああ".bytes
  }

  def createJson(String name, Integer size, String type, String payload) {
    return """
    {
      "meta": {
        "name": ${name ? '"' + name + '"' : "null"},
        "size": ${size ? size : "null"},
        "type": ${type ? '"' + type + '"' : "null"}
      },
      "payload": ${payload ? '"' + payload + '"' : "null"}
    }
    """
  }


  def createExpected(String name, Integer size, String type, String payload) {
    def media = new Media();
    media.meta.name = name != null ? name : "unknown";
    media.meta.size = size != null ? size : 0;
    media.meta.type = type != null ? type : "application/octet-stream";
    media.payload = payload != null ? Base64.getDecoder().decode(payload) : [] as byte[];
    return media;
  }
}
