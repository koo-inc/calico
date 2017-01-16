package jp.co.freemind.calico.config.freemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.jvnet.hk2.annotations.Optional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class FlexibleConfiguration extends Configuration {
    @Inject
    public FlexibleConfiguration(final javax.ws.rs.core.Configuration config, @Optional final ServletContext servletContext) {
        super();

        final List<TemplateLoader> loaders = new ArrayList<>();
        if (servletContext != null) {
            loaders.add(new WebappTemplateLoader(servletContext));
        }
        loaders.add(new ClassTemplateLoader(FlexibleConfiguration.class, "/"));

        // Create Factory.
        this.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
        try {
            settingConfiguration(config);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private void settingConfiguration(javax.ws.rs.core.Configuration rsConfig) throws TemplateException {
        this.setOutputEncoding("UTF-8");
        this.setDefaultEncoding("UTF-8");
        this.setURLEscapingCharset("UTF-8");
        this.setNumberFormat("#");
        this.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

        BeansWrapper wrapper = new BeansWrapper();
        wrapper.setExposeFields(true);
        wrapper.setExposureLevel(BeansWrapper.EXPOSE_SAFE);
        this.setObjectWrapper(wrapper);

        Object object = rsConfig.getProperty(FreemarkerFeature.CONFIGURATION_CUSTOMIZER);
        if (object != null && object instanceof ConfigurationCustomizer) {
            ((ConfigurationCustomizer) object).customize(this);
        }
    }

}
