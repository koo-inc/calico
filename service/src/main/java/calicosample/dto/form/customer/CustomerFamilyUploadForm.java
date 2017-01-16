package calicosample.dto.form.customer;

import javax.validation.constraints.NotNull;

import jp.co.freemind.calico.dto.Form;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter @Setter
public class CustomerFamilyUploadForm extends Form {

  @NotNull
  private Media csv;

  @SneakyThrows
  public String getCsvContent() {
    return new String(csv.getPayload(), "UTF-8");
  }
}
