package jp.co.freemind.calico.core.auth;

import java.io.Serializable;

/**
 * Created by kakusuke on 15/07/27.
 */
public interface AuthInfo extends Serializable {
  Integer getUserId();
  String getLoginId();
  boolean isAuthenticated();

  AuthToken getAuthToken();
}
