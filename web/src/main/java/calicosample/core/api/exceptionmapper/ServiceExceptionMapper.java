package calicosample.core.api.exceptionmapper;

import jp.co.freemind.calico.service.exception.ServiceException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
@Priority(Priorities.USER)
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {
  @Override
  public Response toResponse(ServiceException exception) {
    Map<String, Object> entity = new LinkedHashMap<String, Object>(){{
        put("type", "service-exception");
        put("violations", exception.getViolations());
    }};
    return Response
        .status(Response.Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON)
        .entity(entity)
        .build();
  }
}
