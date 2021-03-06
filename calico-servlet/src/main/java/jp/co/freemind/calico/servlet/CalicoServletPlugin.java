package jp.co.freemind.calico.servlet;

import com.google.inject.Provider;

import jp.co.freemind.calico.core.config.CalicoPlugin;

public class CalicoServletPlugin extends CalicoPlugin {

  @Override
  protected void configure() {
    bind(binder -> {
//      binder.bind(Keys.PATH)
//        .toProvider(needsBinding("PATH"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.REMOTE_ADDRESS)
//        .toProvider(needsBinding("REMOTE_ADDRESS"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.PROCESS_DATETIME)
//        .toProvider(needsBinding("PROCESS_DATETIME"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.AUTH_TOKEN)
//        .toProvider(needsBinding("AUTH_TOKEN"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.SERVLET_REQUEST)
//        .toProvider(needsBinding("HTTP_SERVLET_REQUEST"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.SERVLET_RESPONSE)
//        .toProvider(needsBinding("HTTP_SERVLET_RESPONSE"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.INPUT)
//        .toProvider(needsBinding("INPUT"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Keys.LOGGING_SESSION)
//        .toProvider(needsBinding("LOGGING_SESSION"))
//        .in(TransactionScoped.class);
//
//      binder.bind(Context.class)
//        .toProvider(needsBinding("CONTEXT"))
//        .in(TransactionScoped.class);
    });
  }

  private <T> Provider<T> needsBinding(String key) {
    return() -> {
      throw new IllegalStateException(key + " should be bound to provider.");
    };
  }
}
