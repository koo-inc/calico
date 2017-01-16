package jp.co.freemind.calico.core.auth

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
/**
 * Created by tasuku on 15/03/25.
 */
class AuthTokenTest extends Specification {
  def setupSpec() {
    Env.init([
      injectorFactory: "helper.CalicoTestInjectorFactory",
      secretToken: "w1EnfE@Xjo;_YbA="
    ] as Properties)
  }

  def "復号できること"() {
    when:
    def token = AuthToken.create(input, '127.0.0.1', LocalDateTime.now())
    def otherToken = AuthToken.of(token.value)

    then:
    token.id == output
    and:
    otherToken.id == output
    and:
    token.createdAt.truncatedTo(ChronoUnit.SECONDS) == otherToken.createdAt.truncatedTo(ChronoUnit.SECONDS)

    where:
    input | output
    'abc' | 'abc'
    '123' | '123'
  }

  def "複数回実行時、同じ平文から同じ暗号文が生成されないこと"() {
    when:
    def token1 = AuthToken.create('aaa', '127.0.0.1', LocalDateTime.now())
    def token2 = AuthToken.create('aaa', '127.0.0.1', LocalDateTime.now())

    then:
    token1.value != token2.value
  }

  def "不正な文字列が渡された場合は IllegalArgumentsException が発生すること"() {
    when:
    AuthToken.of('aaaaaa')

    then:
    thrown(IllegalArgumentException)
  }

  def "IPアドレスが復号化されること"() {
    when:
    def token = AuthToken.create("aaa", '127.0.0.1', LocalDateTime.now())
    def otherToken = AuthToken.of(token.value)

    then:
    token.remoteAddress == '127.0.0.1'
    and:
    otherToken.remoteAddress == '127.0.0.1'
  }

  def "CSRFトークンが復号化されること"() {
    when:
    def token = AuthToken.create("aaa", "127.0.0.1", LocalDateTime.now())
    def decryptedToken = AuthToken.of(token.value)
    def regeneratedToken = token.regenerate()

    then:
    token.csrfToken == decryptedToken.csrfToken
    and:
    token.csrfToken == regeneratedToken.csrfToken
  }
}
