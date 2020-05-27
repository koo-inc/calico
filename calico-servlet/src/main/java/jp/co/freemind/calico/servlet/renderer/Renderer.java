package jp.co.freemind.calico.servlet.renderer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.endpoint.result.ResultType;
import jp.co.freemind.calico.core.di.InjectorRef;

public interface Renderer<T> {
  default void render(ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable T output) {
    if (res.isCommitted()) return;

    setStatus(conf, res, resultType, output);
    setHeader(conf, res, resultType, output);
    setCookie(conf, res, resultType, output);

    try (PrintWriter writer = res.getWriter()) {
      writeBody(writer, conf, res, resultType, output);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  default void setStatus(ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable T t) {
    res.setStatus(resultType.getStatus());
  }

  default void setHeader(ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable T t) {
    res.setHeader("Content-Type", resultType.getMimeType());
    res.setCharacterEncoding("utf-8");
  }

  default void setCookie(ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable T t) {
  }

  default void writeBody(PrintWriter writer, ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable T t) throws IOException {
    InjectorRef.getCurrent().getInstance(ObjectMapper.class).writeValue(writer, t);
  }
}
