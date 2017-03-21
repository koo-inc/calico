package jp.co.freemind.calico.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.zone.Zone;

public class DefaultRenderer implements Renderer<Object> {
  private final int status;

  DefaultRenderer(int status) {
    this.status = status;
  }

  @Override
  public void render(@Nullable Object output) {
    Zone current = Zone.getCurrent();
    HttpServletResponse res = current.getInstance(Keys.SERVLET_RESPONSE);
    if (res.isCommitted()) return;

    res.setStatus(status);
    res.setHeader("Content-Type", "application/json");
    res.setCharacterEncoding("utf-8");

    try (PrintWriter writer = res.getWriter()) {
      Zone.getCurrent().getInstance(ObjectMapper.class).writeValue(writer, output);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
