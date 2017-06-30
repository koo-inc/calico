package jp.co.freemind.calico.servlet.renderer;

import static jp.co.freemind.calico.core.endpoint.result.ResultType.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletResponse;

import jp.co.freemind.calico.core.endpoint.exception.UnknownEndpointException;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.exception.InconsistentDataException;
import jp.co.freemind.calico.core.exception.VerificationException;
import jp.co.freemind.calico.core.validation.Violation;

public class ExceptionRenderer implements Renderer<Object> {
  public void render(ServletConfig conf, HttpServletResponse res, Throwable t) {
    if (t instanceof UnknownEndpointException) {
      render(conf, res, notFound(JSON), t);
    }
    else if (t instanceof VerificationException) {
      Violation violation = ((VerificationException) t).getViolation();

      if (t instanceof InconsistentDataException) {
        render(conf, res, conflict(JSON), violation);
      } else if (t instanceof AuthorizationException) {
        render(conf, res, forbidden(JSON), violation);
      } else {
        render(conf, res, invalid(JSON), violation);
      }
    }
  }
}
