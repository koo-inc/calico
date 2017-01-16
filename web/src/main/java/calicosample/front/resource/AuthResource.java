package calicosample.front.resource;

import calicosample.core.auth.NoAuth;
import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@NoAuth
public class AuthResource extends FrontResource {

  @Path("login")
  @GET
  public Viewable login(){
    return viewable("/login.ftl");
  }

}
