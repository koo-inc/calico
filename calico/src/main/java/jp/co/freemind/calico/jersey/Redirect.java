package jp.co.freemind.calico.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.URISyntaxException;

public final class Redirect {
    private Redirect() {
    }

    public static Response to(HttpServletRequest request, String path) {
        return to(request, path, true);
    }

    public static Response to(HttpServletRequest request, String path, boolean sameApp) {
        StringBuilder fullPath = new StringBuilder(request.getContextPath());
        if (sameApp) {
            fullPath.append(request.getServletPath());
        }
        fullPath.append(path);
        try {
            return Response.seeOther(new URI(fullPath.toString())).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
