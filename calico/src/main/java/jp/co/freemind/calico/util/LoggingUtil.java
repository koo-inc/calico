package jp.co.freemind.calico.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by kakusuke on 15/07/02.
 */
public interface LoggingUtil {
  static String collectAllCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return "";

    return Stream.of(cookies)
      .map(c -> c.getName() + "=" + c.getValue())
      .collect(Collectors.joining("\n"));
  }
  static String collectAllHeaders(HttpServletRequest request) {
    if (request.getHeaderNames() == null) return "";

    Stream<String> name = StreamUtil.of(request.getHeaderNames());
    Stream<Stream<String>> values = StreamUtil.of(request.getHeaderNames()).map(request::getHeaders).map(StreamUtil::of);
    return StreamUtil.zip(name, values)
      .flatMap(pair -> (Stream<String>) pair.getRight().map(v -> pair.getLeft() + ": " + v))
      .collect(Collectors.joining("\n"));
  }
}
