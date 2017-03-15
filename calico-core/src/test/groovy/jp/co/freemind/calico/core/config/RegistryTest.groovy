package jp.co.freemind.calico.core.config

import helper.EnvironmentVariables
import spock.lang.Specification

class RegistryTest extends Specification {

  def "load properties"() {
    when:
    def registry = new Registry("env-test/test.properties")

    then:
    assert registry.getValue("test.prop1") == "あああ"
  }

  def "setting param"() {
    when:
    System.setProperty('test.prop2', 'iii')
    new EnvironmentVariables().set('test.prop3', '333')
    def registry = new Registry("env-test/test.properties")
    def setting = registry.loadSetting(SampleSetting)

    then:
    assert setting.prop1 == 'あああ'
    assert setting.prop2 == 'iii'
    assert setting.prop3 == '333'
  }
}

@Setting("test")
interface SampleSetting {
  String getProp1()
  String getProp2()
  String getProp3()
}
