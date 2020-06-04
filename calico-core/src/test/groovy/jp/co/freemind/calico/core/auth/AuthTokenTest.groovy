package jp.co.freemind.calico.core.auth

import com.google.inject.Binder
import jp.co.freemind.calico.core.di.InjectorRef
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AuthTokenTest extends Specification {
  def setupSpec() {
    InjectorRef.initialize() { s -> s.modules(new MockModule()) }
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

  def "不正な文字列が渡された場合は nullを返すこと"() {
    when:
    def token = AuthToken.of('aaaaaa')

    then:
    token == null
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
    def regeneratedToken = token.regenerate(LocalDateTime.now())

    then:
    token.csrfToken == decryptedToken.csrfToken
    and:
    token.csrfToken == regeneratedToken.csrfToken
  }

  private static class MockModule implements com.google.inject.Module {

    @Override
    void configure(Binder binder) {
      binder.bind(AuthSetting).toInstance(new AuthSetting() {
        @Override
        String getSecretToken() {
          return "1234567890123456"
        }
      })
    }
  }
}
