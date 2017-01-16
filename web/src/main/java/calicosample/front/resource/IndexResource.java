package calicosample.front.resource;

import calicosample.core.front.FrontResource;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class IndexResource extends FrontResource {

    @GET
    public Viewable index() {
        return viewable("/main.ftl");
    }
}
