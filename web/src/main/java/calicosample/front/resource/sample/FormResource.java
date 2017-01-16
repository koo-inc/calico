package calicosample.front.resource.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

@Path("sample/form")
public class FormResource extends FrontResource {

  @Path("{path:text|password|radio|radios|checkbox|checkboxes|select|datepicker|timepicker|textarea|file|submit|errors}")
  @GET
  public Viewable base() {
    return viewable("/main.ftl");
  }
}
