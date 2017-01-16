package calicosample.api.resource.system;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;
import calicosample.dto.form.system.JsLogForm;
import calicosample.service.system.LoggingService;

@Path("system")
@NoAuth
public class LoggingApiResource extends ApiResource {
  @Inject
  private LoggingService loggingService;

  @Path("js_log")
  @POST
  public void login(JsLogForm form) {
    loggingService.insert(form);
  }
}
