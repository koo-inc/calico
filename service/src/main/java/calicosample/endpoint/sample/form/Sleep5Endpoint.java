package calicosample.endpoint.sample.form;

import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;
import lombok.SneakyThrows;

public class Sleep5Endpoint implements Endpoint<EmptyInput, EmptyOutput> {

  @Override
  @SneakyThrows
  public EmptyOutput execute(EmptyInput input) {
    Thread.sleep(5000);
    return EmptyOutput.getInstance();
  }
}
