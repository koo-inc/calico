package calicosample.endpoint.sample.submit;

import javax.annotation.Nullable;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;
import lombok.Data;

public class WaitEndpoint implements Endpoint<WaitEndpoint.Input, EmptyOutput> {

  @Override
  public EmptyOutput execute(Input input) {
    try {
      Thread.sleep((input.time != null ? input.time : 0) * 1000);
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  @Data
  public static class Input {
    @Nullable
    private Long time;
  }
}
