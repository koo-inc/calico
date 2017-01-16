package calicosample.core.api.exceptionmapper;

import calicosample.core.auth.AuthenticationRequiredException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticationRequiredExceptionMapper implements ExceptionMapper<AuthenticationRequiredException> {
  @Context HttpServletRequest request;

  @Override
  public Response toResponse(AuthenticationRequiredException exception) {
    Map<String, Object> entity = new LinkedHashMap<String, Object>(){{
      put("type", "authentication-required");
    }};
    return Response
        .status(Response.Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON)
        .entity(entity)
        .build();
  }
}
