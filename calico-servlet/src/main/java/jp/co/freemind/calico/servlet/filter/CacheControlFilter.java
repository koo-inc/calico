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

import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.servlet.assets.AssetsSetting;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@WebFilter(filterName="CacheControlFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class CacheControlFilter implements Filter {

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final AssetsSetting assetsSetting = buildAssetsSetting();
  private static AssetsSetting buildAssetsSetting() {
    return Zone.getCurrent().getInstance(AssetsSetting.class);
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
    long time = Zone.getCurrent().getInstance(SystemSetting.class).version();
    lastModified = time - (time % 1000);
    log.info("version: " + lastModified);
    return lastModified;
  }
}
