package calicosample.endpoint.sample.form;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.jackson.media.WithPayload;
import lombok.Data;
import lombok.Value;

public class EchoFileEndpoint implements Endpoint<EchoFileEndpoint.Input, EchoFileEndpoint.Output> {

  @Override
  public Output execute(Input input) {
    return new Output(input.media);
  }

  @Data
  public static class Input  {
    private Media media;
  }

  @Value
  public static class Output {
    @WithPayload
    private Media media;
  }

}
