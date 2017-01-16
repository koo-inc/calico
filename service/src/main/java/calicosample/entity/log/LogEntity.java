package calicosample.entity.log;

import java.time.LocalDateTime;

import calicosample.core.doma.InjectLogConfig;
import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;

/**
 * Created by tasuku on 15/03/10.
 */
@Entity(listener = LogEntity.LogEntityListener.class)
@InjectLogConfig
@Getter @Setter
public abstract class LogEntity implements Log {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "log_id_seq")
  private Long id;
  private LocalDateTime ts;
  private String key;
  private Long deployedAt;

  public LogEntity() {
    ts = LocalDateTime.now();
    deployedAt = Zone.getCurrent().getInstance(SystemSetting.class).deployedAt();
  }

  public static class LogEntityListener<T extends LogEntity> implements EntityListener<T> {
    @Override public void preInsert(T entity, PreInsertContext<T> context) {
      entity.preInsert();
    }
  }
}
