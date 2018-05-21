package jp.co.freemind.calico.servlet.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

public interface NetworkUtil {

  static String getRemoteAddrWithConsiderForwarded(HttpServletRequest request){
    if (ProxyHolder.PROXIES.contains(request.getRemoteAddr())) {
      String addr = request.getHeader("X-FORWARDED-FOR");
      return Strings.isNullOrEmpty(addr) ? request.getRemoteAddr() : addr;
    }
    else {
      return request.getRemoteAddr();
    }
  }

  static void addProxy(String ip) {
    ProxyHolder.PROXIES.add(ip);
  }

  static boolean removeProxy(String ip) {
    return ProxyHolder.PROXIES.remove(ip);
  }

  final class ProxyHolder {
    private static final Set<String> PROXIES = ConcurrentHashMap.newKeySet();
  }
}
