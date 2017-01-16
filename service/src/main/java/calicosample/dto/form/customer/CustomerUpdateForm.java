package calicosample.dto.form.customer;

import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import jp.co.freemind.calico.dto.DTOUtil;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class CustomerUpdateForm extends CustomerGeneralForm {

  @NotNull
  private Integer id;

  /**
   * create
   */
  public static CustomerUpdateForm create(Customer customer, List<CustomerFamily> customerFamilies){
    CustomerUpdateForm form = DTOUtil.copyProperties(new CustomerUpdateForm(), customer);
    for(CustomerFamily customerFamily : customerFamilies){
      form.getFamilies().add(DTOUtil.copyProperties(new Family(), customerFamily));
    }
    return form;
  }

}
