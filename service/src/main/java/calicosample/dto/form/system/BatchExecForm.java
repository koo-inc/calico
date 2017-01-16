package calicosample.dto.form.system;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import jp.co.freemind.calico.dto.Form;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BatchExecForm extends Form {
  @NotNull
  private String index;
  private Optional<LocalDateTime> targetDateTime;
}
