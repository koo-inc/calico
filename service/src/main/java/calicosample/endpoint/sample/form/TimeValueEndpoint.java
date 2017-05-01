package calicosample.endpoint.sample.form;

import java.time.LocalTime;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import lombok.Value;

public class TimeValueEndpoint implements Endpoint<EmptyInput,TimeValueEndpoint.Output> {
  @Override
  public Output execute(EmptyInput emptyInput) {
    return new Output(LocalTime.of(6, 12));
  }

  @Value
  public static class Output {
    private LocalTime value;
  }
}
