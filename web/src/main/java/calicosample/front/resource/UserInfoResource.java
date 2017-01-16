package calicosample.front.resource;

import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("user_info")
public class UserInfoResource extends FrontResource {

  @Path("{path:index}")
  @GET
  public Viewable base(){
    return viewable("/main.ftl");
  }

}
