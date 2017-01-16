package calicosample.entity;

import java.time.LocalDateTime;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import com.google.inject.TypeLiteral;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.doma.InjectConfig;
import jp.co.freemind.calico.auth.AuthInfo;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tasuku on 15/03/10.
 */
@Entity(listener = CalicoSampleEntity.CalicoSampleEntityListener.class)
@InjectConfig
@Getter @Setter
public abstract class CalicoSampleEntity {
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private String updatedBy;

  public static class CalicoSampleEntityListener<T extends CalicoSampleEntity> implements EntityListener<T> {
    @Override public void preInsert(T entity, PreInsertContext<T> context) {
      entity.setCreatedAt(LocalDateTime.now());
      entity.setCreatedBy(getAuthInfo().getLoginId());
    }
    @Override public void preUpdate(T entity, PreUpdateContext<T> context) {
      entity.setUpdatedAt(LocalDateTime.now());
      entity.setUpdatedBy(getAuthInfo().getLoginId());
    }

    private static final TypeLiteral<Context<CalicoSampleAuthInfo>> contextType = new TypeLiteral<Context<CalicoSampleAuthInfo>>() {};
    private AuthInfo getAuthInfo() {
      return InjectUtil.getInstance(contextType).getAuthInfo();
    }
  }
}
