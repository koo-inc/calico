package calicosample.dto.form.customer;

import lombok.Getter;
import lombok.Setter;
import jp.co.freemind.calico.dto.Form;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class CustomerIdForm extends Form {

  @NotNull
  private Integer id;

  /**
   * create
   */
  public static CustomerIdForm create(Integer id){
    CustomerIdForm form = new CustomerIdForm();
    form.setId(id);
    return form;
  }
}
