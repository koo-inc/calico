package calicosample.api.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;

@Path("system/js_log")
@NoAuth
public class JsLogApiResource extends ApiResource {
  @POST
  public Object post(){
    return super.post();
  }
}
