package calicosample.core.options;

import java.util.Optional;

public interface OptionsSupplier {
  public boolean isKeyMatch(String key);
  public Optional<OptionsRecord> get(OptionsForm form);
}
