package calicosample.core.front;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.co.freemind.calico.context.Context;
import calicosample.core.front.module.Angular;
import calicosample.core.front.module.AngularStrap;
import calicosample.core.front.module.App;
import calicosample.core.front.module.ApplicationInit;
import calicosample.core.front.module.Calico;
import calicosample.core.front.module.CalicoSample;
import calicosample.core.front.module.JQuery;
import calicosample.core.front.module.Sugar;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.jersey.MapViewable;

@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public abstract class FrontResource {
  @Inject private Context context;

  protected MapViewable viewable(String template){
    return new MapViewable(template)
      .add("ENV", Env.getEnvName())
      .add("DEPLOYED_AT", getDeployedAtAsString())
      .add("AUTH_INFO", context.getAuthInfo())
      .add("RESOURCE_ROOT", getResourceRoot())
      .module(
        new JQuery(),
        new Angular(),
        new AngularStrap(),
        new Sugar(),
        new Calico(),
        new CalicoSample(),
        new ApplicationInit(),
        new App(getClass())
      );
  }

  private String getResourceRoot(){
    Path path = getClass().getAnnotation(Path.class);
    if(path == null) throw new RuntimeException("cannot find Path Annotation");
    return path.value();
  }

  private String getDeployedAtAsString(){
    return String.valueOf(Env.getDeployedAt());
  }
}
