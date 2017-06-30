package jp.co.freemind.calico.servlet.renderer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.endpoint.result.ResultType;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.util.CookieUtil;

public class DefaultRenderer implements Renderer<Object> {
  public void render(ServletConfig conf, HttpServletResponse res, Context context, @Nullable Object object) {
    if (object instanceof Result) {
      render(conf, res, ResultType.JSON, object);
    }
    else {
      render(conf, res, ResultType.JSON, new Result(context, object));
    }
  }

  @Override
  public void setCookie(ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable Object o) {
    if (o == null) return;
    ServletContext servletContext = conf.getServletContext();

    ((Result) o).getContext().getAuthInfo()
      .map(AuthInfo::getAuthToken)
      .ifPresent(token -> {
        CookieUtil.setSessionToken(servletContext, res, token);
        CookieUtil.setXsrfToken(servletContext, res, token);
      });
  }

  @Override
  public void writeBody(PrintWriter writer, ServletConfig conf, HttpServletResponse res, ResultType resultType, @Nullable Object o) throws IOException {
    if (o == null) return;
    Zone.getCurrent().getInstance(ObjectMapper.class).writeValue(writer, ((Result) o).getOutput());
  }
}
