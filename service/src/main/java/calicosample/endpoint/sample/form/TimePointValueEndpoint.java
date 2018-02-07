package calicosample.endpoint.sample.form;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.time.TimePoint;
import lombok.Data;
import lombok.Value;

public class TimePointValueEndpoint implements Endpoint<TimePointValueEndpoint.Input,TimePointValueEndpoint.Output> {
  @Override
  public Output execute(TimePointValueEndpoint.Input input) {
    return new Output(input.value);
  }

  @Data
  public static class Input {
    private TimePoint value;
  }

  @Value
  public static class Output {
    private TimePoint value;
  }
}
