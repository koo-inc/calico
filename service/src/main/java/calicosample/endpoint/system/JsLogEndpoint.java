package calicosample.endpoint.system;

import javax.inject.Inject;

import calicosample.dto.form.system.JsLogForm;
import calicosample.service.system.LoggingService;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import lombok.Getter;
import lombok.Setter;

public class JsLogEndpoint extends Endpoint<JsLogForm, EmptyOutput> {
  @Inject private LoggingService loggingService;

  @Getter @Setter
  public static class Input {
    private String location;
    private String userAgent;
    private String exception;
  }

  @Override
  public EmptyOutput execute(JsLogForm input) {
    loggingService.insert(input);
    return EmptyOutput.getInstance();
  }
}
