package calicosample.core.api.exceptionmapper;

import calicosample.core.auth.CsrfException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.LinkedHashMap;
import java.util.Map;

public class CsrfExceptionMapper implements ExceptionMapper<CsrfException> {
  @Context HttpServletRequest request;

  @Override
  public Response toResponse(CsrfException exception) {
    Map<String, Object> entity = new LinkedHashMap<>();
    entity.put("type", "csrf-token-invalid");
    return Response
        .status(Response.Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON)
        .entity(entity)
        .build();
  }
}
