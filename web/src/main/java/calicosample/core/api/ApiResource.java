package calicosample.core.api;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.EndpointManager;
import lombok.SneakyThrows;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class ApiResource {
  @Inject private EndpointManager endpointManager;
  @Inject private ObjectMapper objectMapper;

  @Context
  private ContainerRequestContext requestContext;

  @Path("{path:.*}")
  @POST
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public Object post(){
    String path = requestContext.getUriInfo().getPath();
    Endpoint endpoint = endpointManager.getEndpoint(path);
    Type inputType = endpointManager.getInputType(path);
    Object input = objectMapper.readValue(requestContext.getEntityStream(), objectMapper.constructType(inputType));
    return endpoint.execute(input);
  }

}
