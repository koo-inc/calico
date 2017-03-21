package jp.co.freemind.calico.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.util.SessionUtil;

public class ResultRenderer implements Renderer<Result> {
  @Override
  public void render(Result result) {
    Zone current = Zone.getCurrent();
    HttpServletResponse res = current.getInstance(Keys.SERVLET_RESPONSE);
    if (res.isCommitted()) return;

    ServletContext servletContext = current.getInstance(Keys.SERVLET_REQUEST).getServletContext();

    res.setStatus(result.getResultType().getStatus());
    res.setHeader("Content-Type", result.getResultType().getMimeType());
    res.setCharacterEncoding("utf-8");
    result.getContext().getAuthInfo()
      .map(AuthInfo::getAuthToken)
      .ifPresent(token -> {
        SessionUtil.setSessionToken(servletContext, res, token);
        SessionUtil.setXsrfToken(servletContext, res, token);
      });

    try (PrintWriter writer = res.getWriter()) {
      Zone.getCurrent().getInstance(ObjectMapper.class).writeValue(writer, result.getOutput());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
