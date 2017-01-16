package calicosample.dto.form.customer;

import javax.validation.constraints.NotNull;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerFamilyDownloadForm extends Form {

  @NotNull
  private Integer id;
}
