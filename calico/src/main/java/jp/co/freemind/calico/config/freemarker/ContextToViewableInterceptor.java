package jp.co.freemind.calico.config.freemarker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Providers;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.ResolvedViewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;
import org.glassfish.jersey.server.mvc.spi.ViewableContext;

@Priority(Priorities.ENTITY_CODER + ContextToViewableInterceptor.PRIORITY)
public class ContextToViewableInterceptor implements WriterInterceptor {
    public static final int PRIORITY = 10;
    private static final String CONTEXT_KEY_NAME = "context";

    @Context HttpServletRequest servletRequest;
    @Context ResourceInfo resourceInfo;

    @Inject private ServiceLocator serviceLocator;
    @Inject private Provider<ViewableContext> viewableContextProvider;

    @SuppressWarnings("unchecked")
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException {
        final Object entity = context.getEntity();

        if (entity instanceof Viewable) {
            final Viewable viewable = (Viewable) entity;
            Object model = viewable.getModel();
            if (!(model instanceof Map)) {
                model = new HashMap<>();
                ((Map<String, Object>) model).put("model", viewable.getModel());
            }
            Map<String, Object> map = (Map<String, Object>) model;
            setHttpContext(map, servletRequest);
            map.put("toJson", new ToJson());

            Viewable newViewable = new Viewable(viewable.getTemplateName(), model);
            if (resourceInfo.getResourceMethod().getAnnotation(Produces.class) == null) {
                ResolvedViewable<?> resolvedViewable = createResolvedViewable(newViewable);
                if (resolvedViewable != null) {
                    context.setEntity(resolvedViewable);
                } else {
                    context.setEntity(newViewable);
                }
            } else {
                context.setEntity(newViewable);
            }
        }

        context.proceed();
    }

    private void setHttpContext(Map<String, Object> map, HttpServletRequest httpServletRequest) {
        if (map.containsKey(CONTEXT_KEY_NAME)) {
            return;
        }
        Map<String, Object> context = new HashMap<>();
        if (httpServletRequest != null) {
            context.put("path", httpServletRequest.getServletPath() + httpServletRequest.getPathInfo());
            context.put("appPath", httpServletRequest.getContextPath() + httpServletRequest.getServletPath());
            context.put("fullPath", httpServletRequest.getContextPath() + httpServletRequest.getServletPath() + httpServletRequest.getPathInfo());
            context.put("cookies", httpServletRequest.getCookies());
            context.put("locale", httpServletRequest.getLocale());
        }
        map.put(CONTEXT_KEY_NAME, context);
    }

    private ResolvedViewable<?> createResolvedViewable(Viewable viewable) {
        for (TemplateProcessor<?> processor : Providers.getCustomProviders(serviceLocator, TemplateProcessor.class)) {
            ResolvedViewable<?> resolvedViewable = viewableContextProvider.get().resolveViewable(viewable,
                    MediaType.TEXT_HTML_TYPE, resourceInfo.getResourceClass(), processor);
            if (resolvedViewable != null) {
                return resolvedViewable;
            }
        }
        return null;
    }
}
