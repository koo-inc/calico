package helper

import calicosample.core.auth.CalicoSampleAuthInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import jp.co.freemind.calico.auth.AuthToken
import jp.co.freemind.calico.config.jackson.ObjectMapperProvider
import jp.co.freemind.calico.context.Context
import jp.co.freemind.calico.context.ContextProxy

import java.time.LocalDateTime
/**
 * Created by tasuku on 15/04/13.
 */
class TestServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ObjectMapper).toInstance(ObjectMapperProvider.createMapper())

    Context.Builder<CalicoSampleAuthInfo> builder = Context.builder()
    bind(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {}).toInstance(new ContextProxy(
      builder
        .authToken(AuthToken.createEmpty("localhost", LocalDateTime.now()))
        .authInfo(CalicoSampleAuthInfo.ofNull())
        .processDateTime(LocalDateTime.now())
        .remoteAddress("127.0.0.1")
        .build()
    ))
  }
}
