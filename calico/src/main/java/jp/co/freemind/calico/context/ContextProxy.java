package jp.co.freemind.calico.context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import jp.co.freemind.calico.auth.AuthInfo;
import jp.co.freemind.calico.auth.AuthToken;

/**
 * Created by kakusuke on 15/07/16.
 */
@RequestScoped
public class ContextProxy<T extends AuthInfo, U extends Context<T>> implements Context<T> {
  private Deque<Context<T>> contextStack = new ArrayDeque<>();

  @Inject
  public ContextProxy(U context) {
    contextStack.push(context);
  }

  @Override
  public void switchTo(Context<T> context, Runnable runnable) {
    contextStack.push(context);
    try {
     runnable.run();
    }
    finally {
      contextStack.pop();
    }
  }

  @Override
  public void switchTo(Context<T> context) {
    contextStack.pop();
    contextStack.push(context);
  }

  @Override
  public AuthToken getAuthToken() {
    return getContext().getAuthToken();
  }

  @Override
  public T getAuthInfo() {
    return getContext().getAuthInfo();
  }

  @Override
  public LocalDateTime getProcessDateTime() {
    return getContext().getProcessDateTime();
  }

  @Override
  public LocalDate getProcessDate() {
    return getContext().getProcessDate();
  }

  @Override
  public String getRemoteAddress() {
    return getContext().getRemoteAddress();
  }

  private Context<T> getContext() {
    return contextStack.peek();
  }
}
