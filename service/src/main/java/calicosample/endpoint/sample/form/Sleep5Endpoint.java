package calicosample.endpoint.sample.form;

import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import lombok.SneakyThrows;

public class Sleep5Endpoint extends Endpoint<EmptyInput, EmptyOutput> {

  @Override
  @SneakyThrows
  public EmptyOutput execute(EmptyInput input) {
    Thread.sleep(5000);
    return EmptyOutput.getInstance();
  }
}
