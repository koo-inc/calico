package calicosample.api.resource;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.auth.NoAuth;
import calicosample.dto.form.auth.LoginForm;
import calicosample.service.AuthService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("auth")
@NoAuth
public class AuthApiResource extends ApiResource {
  @Inject private AuthService authService;

  @Path("login")
  @POST
  public CalicoSampleAuthInfo login(LoginForm form) {
    return authService.login(form);
  }

  @Path("logout")
  @POST
  public Object logout() {
    authService.logout();
    return null;
  }

}
