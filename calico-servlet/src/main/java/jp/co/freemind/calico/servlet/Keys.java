package jp.co.freemind.calico.servlet;

import java.io.InputStream;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Key;
import com.google.inject.name.Names;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.log.LoggingSession;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.servlet.renderer.DefaultRenderer;
import jp.co.freemind.calico.servlet.renderer.ExceptionRenderer;

final public class Keys {
  public static final Key<String> PATH = Key.get(String.class, Names.named("path"));
  public static final Key<String> REMOTE_ADDRESS = Key.get(String.class, Names.named("remoteAddress"));
  public static final Key<AuthToken> AUTH_TOKEN = Key.get(AuthToken.class);
  public static final Key<LocalDateTime> PROCESS_DATETIME = Key.get(LocalDateTime.class, Names.named("processDatetime"));
  public static final Key<Context> CONTEXT = Key.get(Context.class);
  public static final Key<LoggingSession> LOGGING_SESSION = Key.get(LoggingSession.class);
  public static final Key<HttpServletResponse> SERVLET_RESPONSE = Key.get(HttpServletResponse.class);
  public static final Key<HttpServletRequest> SERVLET_REQUEST = Key.get(HttpServletRequest.class);
  public static final Key<InputStream> INPUT = Key.get(InputStream.class, Names.named("input"));

  public static final Key<DefaultRenderer> DEFAULT_RENDERER = Key.get(DefaultRenderer.class);
  public static final Key<ExceptionRenderer> EXCEPTION_RENDERER = Key.get(ExceptionRenderer.class);
}
