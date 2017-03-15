package calicosample.endpoint.sample.form;

import java.util.List;

import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.validation.Violation;
import lombok.Getter;
import lombok.Setter;

public class ValidationArrayEndpoint extends ValidationBaseEndpoint<ValidationArrayEndpoint.Input, ValidationArrayEndpoint.Output> {

  @Override
  public Output execute(Input input) {
    return null;
  }

  @Getter @Setter
  public static class Input extends ValidationBaseEndpoint.Input  {
    private List<Child> children;
    @Validate
    public void validateInput(Violation violation) {
      violation.mark("id", "エラー1");
    }
  }

  @Getter @Setter
  public static class Child {
    private Integer id;
    private String name;

    @Validate
    public void validateInput(Violation violation) {
      violation.mark("id", "エラー2");
    }
  }
  public static class Output extends ValidationBaseEndpoint.Output {

  }

}
