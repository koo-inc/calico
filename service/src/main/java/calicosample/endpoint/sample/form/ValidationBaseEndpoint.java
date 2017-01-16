package calicosample.endpoint.sample.form;

import calicosample.core.validator.AllowEmpty;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

public abstract class ValidationBaseEndpoint<I extends ValidationBaseEndpoint.Input, O extends ValidationBaseEndpoint.Output> implements Endpoint<I, O> {

  @Getter @Setter
  public static class Input {
    private Integer id;

    @AllowEmpty
    private String name;
  }

  @Getter @Setter
  public static class Output {
    private Integer id;
  }
}
