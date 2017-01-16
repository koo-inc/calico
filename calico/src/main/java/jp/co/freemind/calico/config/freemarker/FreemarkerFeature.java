package jp.co.freemind.calico.config.freemarker;

import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class FreemarkerFeature implements Feature {
    public static final String CONFIGURATION_CUSTOMIZER = "calico.configCustomizer";

    @Override
    public boolean configure(FeatureContext context) {
        final Configuration config = context.getConfiguration();

        if (!config.isRegistered(FreemarkerMvcFeature.class)) {
            context.register(FreemarkerMvcFeature.class);

            context.property(FreemarkerMvcFeature.TEMPLATE_OBJECT_FACTORY, FlexibleConfiguration.class);

            if (config.getProperty(FreemarkerMvcFeature.TEMPLATES_BASE_PATH) == null) {
                context.property(FreemarkerMvcFeature.TEMPLATES_BASE_PATH, "/WEB-INF/ftl/template");
            }

            context.register(ContextToViewableInterceptor.class);
            return true;
        }
        return false;
    }
}
