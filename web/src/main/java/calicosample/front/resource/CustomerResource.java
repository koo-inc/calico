package calicosample.front.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("customer")
public class CustomerResource extends FrontResource {

  @Path("{path:index|show|edit}")
  @GET
  public Viewable base(){
    return viewable("/main.ftl");
  }

}
