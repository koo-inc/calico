package jp.co.freemind.calico.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.di.InjectorRef;
import jp.co.freemind.calico.servlet.assets.AssetsSetting;

@WebFilter(filterName="CacheControlFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class CacheControlFilter implements Filter {

  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CacheControlFilter.class);
  private final AssetsSetting assetsSetting = buildAssetsSetting();
  private static AssetsSetting buildAssetsSetting() {
    return InjectorRef.getInstance(AssetsSetting.class);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse res = (HttpServletResponse) servletResponse;

    if (!req.getMethod().equals("GET")) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    long lastModified = getLastModified();

    long ifModifiedSince = req.getDateHeader("If-Modified-Since");
    if (getAssetsSetting().cacheEnabled() && ifModifiedSince >= lastModified) {
      log.trace("304 Not Modified: " + req.getRequestURL());
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      res.setDateHeader("Last-Modified", lastModified);
      res.getWriter().close();
    }
    else {
      res.setDateHeader("Last-Modified", lastModified);
      res.setHeader("Cache-Control", "private, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");

      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() { }

  // If-Modified-Since には秒単位以下の桁はないため
  private static Long lastModified;
  private static long getLastModified() {
    if (lastModified != null) return lastModified;
    long time = InjectorRef.getInstance(SystemSetting.class).version();
    lastModified = time - (time % 1000);
    log.info("version: " + lastModified);
    return lastModified;
  }

  private AssetsSetting getAssetsSetting() {
    return this.assetsSetting;
  }
}
