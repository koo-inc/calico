package jp.co.freemind.calico.servlet;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Key;
import com.google.inject.name.Names;
import jp.co.freemind.calico.core.auth.AuthToken;

final public class Keys {
  public static final Key<String> PATH = Key.get(String.class, Names.named("path"));
  public static final Key<String> REMOTE_ADDRESS = Key.get(String.class, Names.named("remoteAddress"));
  public static final Key<AuthToken> AUTH_TOKEN = Key.get(AuthToken.class, Names.named("authToken"));
  public static final Key<LocalDateTime> PROCESS_DATETIME = Key.get(LocalDateTime.class, Names.named("processDatetime"));
  public static final Key<HttpServletResponse> SERVLET_RESPONSE = Key.get(HttpServletResponse.class, Names.named("servletResponse"));
  public static final Key<HttpServletRequest> SERVLET_REQUEST = Key.get(HttpServletRequest.class, Names.named("servletRequest"));
}
