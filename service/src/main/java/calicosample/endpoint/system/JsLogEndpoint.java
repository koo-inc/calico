package calicosample.endpoint.system;

import javax.inject.Inject;

import calicosample.service.system.LoggingService;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;

public class JsLogEndpoint implements Endpoint<LoggingService.JsLogForm, EmptyOutput> {
  @Inject private LoggingService loggingService;

  @Override
  public EmptyOutput execute(LoggingService.JsLogForm input) {
    loggingService.insert(input);
    return EmptyOutput.getInstance();
  }
}
