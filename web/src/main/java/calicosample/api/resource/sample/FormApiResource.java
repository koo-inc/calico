package calicosample.api.resource.sample;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;
import lombok.SneakyThrows;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("sample/form")
@NoAuth
public class FormApiResource extends ApiResource {

  @Path("sleep5")
  @POST
  @SneakyThrows
  public Object sleep5() {
    Thread.sleep(5000);
    return  null;
  }

}
