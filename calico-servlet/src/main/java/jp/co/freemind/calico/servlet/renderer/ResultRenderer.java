package jp.co.freemind.calico.servlet.renderer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.Keys;
import jp.co.freemind.calico.servlet.util.CookieUtil;

public class ResultRenderer implements Renderer<Result> {
  @Override
  public void render(Result result) {
    HttpServletResponse res = Zone.getCurrent().getInstance(Keys.SERVLET_RESPONSE);
    if (res.isCommitted()) return;

    setStatus(result, res);
    setHeader(result, res);
    setCookie(result, res);

    try (PrintWriter writer = res.getWriter()) {
      Zone.getCurrent().getInstance(ObjectMapper.class).writeValue(writer, result.getOutput());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected void setStatus(Result result, HttpServletResponse res) {
    res.setStatus(result.getResultType().getStatus());
  }

  protected void setHeader(Result result, HttpServletResponse res) {
    res.setHeader("Content-Type", result.getResultType().getMimeType());
    res.setCharacterEncoding("utf-8");
  }

  protected void setCookie(Result result, HttpServletResponse res) {
    ServletContext servletContext = Zone.getCurrent().getInstance(Keys.SERVLET_REQUEST).getServletContext();

    result.getContext().getAuthInfo()
      .map(AuthInfo::getAuthToken)
      .ifPresent(token -> {
        CookieUtil.setSessionToken(servletContext, res, token);
        CookieUtil.setXsrfToken(servletContext, res, token);
      });
  }
}
