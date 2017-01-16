package calicosample.dto.form.system;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JsLogForm extends Form {
  private String location;
  private String userAgent;
  private String exception;
}
