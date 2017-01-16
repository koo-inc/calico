package calicosample.core.api;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import calicosample.core.api.exceptionmapper.AuthenticationRequiredExceptionMapper;
import calicosample.core.api.exceptionmapper.CsrfExceptionMapper;
import calicosample.core.api.exceptionmapper.DefaultExceptionMapper;
import calicosample.core.api.exceptionmapper.ServiceExceptionMapper;
import calicosample.core.auth.AuthFilter;
import calicosample.core.log.LoggingFilter;
import calicosample.core.security.SecurityFilter;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.config.jackson.CustomJacksonFeature;
import jp.co.freemind.calico.config.jackson.ObjectMapperProvider;
import jp.co.freemind.calico.guice.InjectUtil;

@ApplicationPath("api")
public class ApiConfig extends ResourceConfig {

  @Inject
  public ApiConfig(ServiceLocator serviceLocator) {
    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(InjectUtil.getInjector());

    register(ObjectMapperProvider.class);
    register(CustomJacksonFeature.class);

//        register(AuthenticationFilter.class);
//        property(AuthenticationFilter.SESSION_TIMEOUT_RESPONSE, Response.ok(new Viewable("/login.ftl")).type(MediaType.TEXT_HTML_TYPE).build());
//        register(JsonCharsetFilter.class);
//
//    register(AjaxValidationErrorExceptionMapper.class);

    register(LoggingFilter.class);
    register(SecurityFilter.class);
    register(AuthFilter.class);
    register(AuthenticationRequiredExceptionMapper.class);
    register(CsrfExceptionMapper.class);
    register(ServiceExceptionMapper.class);
    register(DefaultExceptionMapper.class);

    packages(Env.getRootPackage() + ".api.resource");
  }
}
