package calicosample.entity.log;

import java.time.LocalDate;
import java.time.LocalTime;

import org.seasar.doma.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakusuke on 15/06/14.
 */
@Entity
@Getter @Setter
public class BatchStartLog extends LogEntity implements StartLog {
  private LocalDate targetDate;
  private LocalTime targetTime;
  private String className;
}
