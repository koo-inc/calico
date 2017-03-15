package calicosample.endpoint.sample.form;

import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.validation.Violation;

public class ValidationObjectEndpoint extends ValidationBaseEndpoint<ValidationObjectEndpoint.Input, ValidationObjectEndpoint.Output> {

  @Override
  public Output execute(Input input) {
    return null;
  }

  public static class Input extends ValidationBaseEndpoint.Input  {
    @Validate
    public void validateInput(Violation violation) {
      violation.mark("id", "エラー1");
    }
  }
  public static class Output extends ValidationBaseEndpoint.Output {

  }
}
