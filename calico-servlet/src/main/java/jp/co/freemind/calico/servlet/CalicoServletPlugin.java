package jp.co.freemind.calico.servlet;

import com.google.inject.Provider;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.config.CalicoPlugin;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;

public class CalicoServletPlugin extends CalicoPlugin {

  @Override
  protected void configure() {
    bind(binder -> {
      binder.bind(Keys.PATH)
        .toProvider(needsBinding("PATH"))
        .in(TransactionScoped.class);

      binder.bind(Keys.REMOTE_ADDRESS)
        .toProvider(needsBinding("REMOTE_ADDRESS"))
        .in(TransactionScoped.class);

      binder.bind(Keys.PROCESS_DATETIME)
        .toProvider(needsBinding("PROCESS_DATETIME"))
        .in(TransactionScoped.class);

      binder.bind(Keys.AUTH_TOKEN)
        .toProvider(needsBinding("AuthToken"))
        .in(TransactionScoped.class);

      binder.bind(Keys.SERVLET_REQUEST)
        .toProvider(needsBinding("HTTP_SERVLET_REQUEST"))
        .in(TransactionScoped.class);

      binder.bind(Keys.SERVLET_RESPONSE)
        .toProvider(needsBinding("HTTP_SERVLET_RESPONSE"))
        .in(TransactionScoped.class);

      binder.bind(Context.class).toProvider(() -> {
        Zone current = Zone.getCurrent();
        AuthenticationProcedure authority = current.getInstance(AuthenticationProcedure.class);
        AuthToken token = current.getInstance(Keys.AUTH_TOKEN);
        return new Context(s -> s
          .authInfo(authority.proceed(token))
          .processDateTime(current.getInstance(Keys.PROCESS_DATETIME))
          .remoteAddress(current.getInstance(Keys.REMOTE_ADDRESS))
          .path(current.getInstance(Keys.PATH))
        );
      }).in(TransactionScoped.class);
    });
  }

  private <T> Provider<T> needsBinding(String key) {
    return() -> {
      throw new IllegalStateException(key + " should be bound to provider.");
    };
  }
}
