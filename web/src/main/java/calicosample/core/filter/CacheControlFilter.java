package calicosample.core.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.jersey.assets.AssetsEnv;
import lombok.extern.log4j.Log4j2;

/**
 * web.xml に以下の設定を追加しておく
 *
 * <pre><code><![CDATA[

<filter>
  <filter-name>cacheControl</filter-name>
  <filter-class>calicosample.core.filter.CacheControlFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>cacheControl</filter-name>
  <url-pattern>/assets/*</url-pattern>
</filter-mapping>

 ]]></pre>
 * Created by tasuku on 15/02/23.
 */
@Log4j2
public class CacheControlFilter implements Filter {
  private static final long LAST_MODIFIED = getCurrentTimestamp();
  private static final AssetsEnv assetsEnv = Env.loadPartial(AssetsEnv.class);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse res = (HttpServletResponse) servletResponse;

    long ifModifiedSince = req.getDateHeader("If-Modified-Since");
    if (assetsEnv.cacheEnabled() && ifModifiedSince >= LAST_MODIFIED) {
      log.trace("304 Not Modified: " + req.getRequestURL());
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      res.setDateHeader("Last-Modified", LAST_MODIFIED);
      res.getWriter().close();
    }
    else {
      res.setDateHeader("Last-Modified", LAST_MODIFIED);
      res.setHeader("Cache-Control", "private, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");

      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }

  @Override
  public void destroy() { }

  // If-Modified-Since には秒単位以下の桁はないため
  private static long getCurrentTimestamp() {
    long time = Env.getDeployedAt();
    time = time - (time % 1000);
    log.info("deployedAt: " + time);
    return time;
  }
}
