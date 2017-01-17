package calicosample.api.resource;

import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;

@Path("/")
@NoAuth
public class DefaultApiResource extends ApiResource {}
