package jp.co.freemind.calico.core.auth;

import java.io.Serializable;

public interface AuthInfo extends Serializable {
  Integer getUserId();
  String getLoginId();
  boolean isAuthenticated();
  boolean hasRights(Rights rights);

  AuthToken getAuthToken();
}
