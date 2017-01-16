package calicosample.front.resource.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.glassfish.jersey.server.mvc.Viewable;
import calicosample.core.front.FrontResource;

@Path("system/batch")
public class BatchResource extends FrontResource {

  @Path("{path:index}")
  @GET
  public Viewable base(){
    return viewable("/main.ftl");
  }

}
