package helper

import calicosample.core.auth.CalicoSampleAuthInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import jp.co.freemind.calico.core.auth.AuthToken
import jp.co.freemind.calico.jackson.ObjectMapperProvider
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

    Context.Builder builder = Context.builder()
    bind(Context).toInstance(new ContextProxy(
      builder
        .authToken(AuthToken.createEmpty("localhost", LocalDateTime.now()))
        .authInfo(CalicoSampleAuthInfo.ofNull("127.0.0.1", LocalDateTime.now()))
        .processDateTime(LocalDateTime.now())
        .remoteAddress("127.0.0.1")
        .build()
    ))
  }
}
