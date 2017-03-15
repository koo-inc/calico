package calicosample.core.options;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractOptionsManager implements OptionsManager {

  public List<OptionsRecord> get(List<OptionsForm> forms) {
    return forms.stream().map(form -> {
      List<OptionsSupplier> suppliers = Arrays.stream(getSuppliers())
        .filter(supplier -> supplier.isKeyMatch(form.getKey()))
        .collect(Collectors.toList());
      for (OptionsSupplier supplier : suppliers) {
        Optional<OptionsRecord> record = supplier.get(form);
        if (record.isPresent()) return record.get();
      }
      throw new OptionsException("unknown options key: " + form.getKey());
    }).collect(Collectors.toList());
  }

  abstract protected OptionsSupplier[] getSuppliers();

}
