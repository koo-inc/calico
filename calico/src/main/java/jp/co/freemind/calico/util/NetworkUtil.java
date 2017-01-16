package jp.co.freemind.calico.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public interface NetworkUtil {

  static String getRemoteAddrWithConsiderForwarded(HttpServletRequest request){
    String addr = request.getHeader("X-FORWARDED-FOR");
    return StringUtils.isBlank(addr) ? request.getRemoteAddr() : addr;
  }
}
