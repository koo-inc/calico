package jp.co.freemind.calico.servlet.util;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

public interface NetworkUtil {

  static String getRemoteAddrWithConsiderForwarded(HttpServletRequest request){
    String addr = request.getHeader("X-FORWARDED-FOR");
    return Strings.isNullOrEmpty(addr) ? request.getRemoteAddr() : addr;
  }
}
