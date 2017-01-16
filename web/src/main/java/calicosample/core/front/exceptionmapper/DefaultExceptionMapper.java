package calicosample.core.front.exceptionmapper;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.service.system.LoggingService;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.util.LoggingUtil;
import lombok.extern.log4j.Log4j2;

@javax.ws.rs.ext.Provider
@Priority(Priorities.USER + 1)
@Log4j2
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {
  @Inject Provider<AccessStartLog> accessStartLogProvider;
  @Inject LoggingService loggingService;
  @Context HttpServletRequest request;

  public DefaultExceptionMapper() {
    InjectUtil.injectMembers(this);
  }

  @Override
  public Response toResponse(Throwable exception) {
    loggingService.insert(ErrorLog.of(accessStartLogProvider.get(), exception, LoggingUtil.collectAllHeaders(request)));
    log.error("{}", Throwables.getRootCause(exception).getClass().getSimpleName(), exception);
    return Response
      .status(Response.Status.INTERNAL_SERVER_ERROR)
      .type(MediaType.TEXT_HTML)
      .entity("<html><h1>500 Internal Server Error</h1><p>" + exception.getMessage() + "</p>")
      .build();
  }
}
