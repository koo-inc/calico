package calicosample.front.resource.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("sample/mail")
public class MailResource extends FrontResource {

  @Path("{path:index}")
  @GET
  public Viewable base() {
    return viewable("/main.ftl");
  }
}
