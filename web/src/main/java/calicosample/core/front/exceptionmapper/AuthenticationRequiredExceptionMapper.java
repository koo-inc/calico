package calicosample.core.front.exceptionmapper;

import calicosample.core.auth.AuthenticationRequiredException;
import jp.co.freemind.calico.jersey.Redirect;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AuthenticationRequiredExceptionMapper implements ExceptionMapper<AuthenticationRequiredException> {
  @Context HttpServletRequest request;

  @Override
  public Response toResponse(AuthenticationRequiredException exception) {
    return Redirect.to(request, "/login");
  }
}
