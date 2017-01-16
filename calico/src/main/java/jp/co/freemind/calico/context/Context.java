package jp.co.freemind.calico.context;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.NotImplementedException;
import jp.co.freemind.calico.auth.AuthInfo;
import jp.co.freemind.calico.auth.AuthToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by tasuku on 15/04/20.
 */
public interface Context<T extends AuthInfo> {
  AuthToken getAuthToken();
  T getAuthInfo();

  String getRemoteAddress();

  LocalDateTime getProcessDateTime();
  default LocalDate getProcessDate() {
    return getProcessDateTime().toLocalDate();
  }

  default void switchTo(Context<T> context, Runnable runnable) {
    throw new NotImplementedException(this.getClass().getName() + "#switchTo is not implemented.");
  }

  default void switchTo(Context<T> context) {
    throw new NotImplementedException(this.getClass().getName() + "#switchTo is not implemented.");
  }

  static <T extends AuthInfo> Builder<T> builder() {
    return new Builder<>();
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  class ContextImpl<T extends AuthInfo> implements Context<T> {
    @NonNull private final AuthToken authToken;
    @NonNull private final T authInfo;
    @NonNull private final LocalDateTime processDateTime;
    @NonNull private final String remoteAddress;
  }

  class Builder<T extends AuthInfo> {
    private Context<T> context;
    @Setter @Accessors(fluent = true) private AuthToken authToken;
    @Setter @Accessors(fluent = true) private T authInfo;
    @Setter @Accessors(fluent = true) private String remoteAddress;
    @Setter @Accessors(fluent = true) private LocalDateTime processDateTime;

    public Builder<T> inheritFrom(Context<T> context) {
      this.context = context;
      return this;
    }

    public Context<T> build() {
      return new ContextImpl<>(
        authToken != null ? authToken : context.getAuthToken(),
        authInfo != null ? authInfo : context.getAuthInfo(),
        processDateTime != null ? processDateTime : context.getProcessDateTime(),
        remoteAddress != null ? remoteAddress : context.getRemoteAddress()
      );
    }
  }
}
