package calicosample.core.options;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OptionsForm {
  private String key;
  private Optional<LocalDateTime> lastModifiedAt;
}
