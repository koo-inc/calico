package calicosample.endpoint.sample.form;

import java.util.Map;

import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.validation.Violation;
import lombok.Getter;
import lombok.Setter;

public class ValidationMapEndpoint extends ValidationBaseEndpoint<ValidationMapEndpoint.Input, ValidationMapEndpoint.Output> {

  @Override
  public Output execute(Input input) {
    return null;
  }

  @Getter @Setter
  public static class Input extends ValidationBaseEndpoint.Input  {
    private Map<String, Child> childMap;
    @Validate
    public void validateInput(Violation violation) {
      violation.mark("id", "エラー1");
      violation.mark("hoge", "間違えたエラー1");
    }
  }

  @Getter @Setter
  public static class Child {
    private Integer id;
    private String name;

    @Validate
    public void validateInput(Violation violation) {
      violation.mark("id", "エラー2");
      violation.mark("hoge", "間違えたエラー2");
    }
  }
  public static class Output extends ValidationBaseEndpoint.Output {

  }

}
