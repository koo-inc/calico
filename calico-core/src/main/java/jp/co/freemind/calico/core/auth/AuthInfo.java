package jp.co.freemind.calico.core.auth;

import java.io.Serializable;
import java.util.Set;

public interface AuthInfo extends Serializable {
  Integer getUserId();
  String getLoginId();
  boolean isAuthenticated();
  Set<Authority> getAuthorities();

  AuthToken getAuthToken();

  default boolean isUsedBySystem() {
    return false;
  }
}
