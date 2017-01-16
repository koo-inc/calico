package calicosample.core.fmstorage

import jp.co.freemind.calico.config.env.Env
import jp.co.freemind.calico.media.Media
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by tasuku on 15/04/27.
 */
class FmStorageTest extends Specification {

  @Shared
  def fmStorage = new FmStorage();

  def setupSpec() {
    Env.init("test");
  }

  def setup() {
    try {
      FmStorageCommand.LIST.execute([path: FmStorageEnv.getEnv().basePath]).each {
        println "deleting: " + it
        FmStorageCommand.DELETE.execute([path: it])
      }
    }
    catch (Exception e) {
      println "no list deleted."
    }
  }

  def "データが登録されていない場合、save すると新規データを登録すること"() {
    when:
    def media = putTestData()

    then:
    assert fmStorage.get(media.id).get().payload == media.payload
    and:
    assert fmStorage.get(media.id).get().meta == media.meta
  }

  def "データが登録されている場合、save すると既存データを置き換えること"() {
    given:
    def media = putTestData()

    when:
    def newMedia = getTestData()
    newMedia.payload = newMedia.class.getResource("/data/test2.txt").bytes
    fmStorage.store(newMedia)

    then:
    assert fmStorage.get(media.id).get().payload == newMedia.payload
  }

  def "データが登録されている場合、delete できること"() {
    given:
    def media = putTestData()

    when:
    fmStorage.remove(media)

    then:
    assert !fmStorage.get(media.id).isPresent()
  }

  def "データが登録されていない場合、delete すると例外が発生しないこと"() {
    given:
    def media = getTestData()

    when:
    fmStorage.remove(media)

    then:
    notThrown(Throwable)
  }

  def "storeされたファイルと同じファイルがgetできること"() {
    given:
    def media = getTestData()
    media.meta.name = "マルチバイトファイル.txt"

    when:
    fmStorage.store(media)
    def stored = fmStorage.get(media.id).get()

    then:
    assert media.meta.name == stored.meta.name
    and:
    assert media.meta.type == stored.meta.type
    and:
    assert media.meta.size == stored.meta.size
    and:
    assert media.id == stored.id
    and:
    assert media.payload == stored.payload
  }

  /*
  def "データが登録されている場合、copy できること"() {
    given:
    def media = putTestData()

    when:
    fmStorage.copy(media.id, media.id + "_copied")

    then:
    assert fmStorage.get(media.id + "_copied").get().content == media.content
  }

  def "データが登録されていない場合、copy すると例外が発生すること"() {
    given:
    def media = putTestData()

    when:
    fmStorage.copy(media.id, media.id + "_copied")

    then:
    thrown()
  }
  */

  def getTestData() {
    def media = new Media()
    media.id = "my/test1_txt"
    media.payload = media.class.getResource("/data/test1.txt").bytes
    media.meta.name = "my_test1.txt"
    media.meta.type = "text/plain"
    media.meta.size = media.payload.length
    return media;
  }

  def putTestData() {
    def media = getTestData()
    fmStorage.store(media)
    return media;
  }
}
