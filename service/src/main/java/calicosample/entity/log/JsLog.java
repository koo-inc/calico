package calicosample.entity.log;

import javax.annotation.Nullable;

import org.seasar.doma.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakusuke on 15/06/14.
 */
@Entity
@Getter @Setter
public class JsLog extends LogEntity {
  private Integer userId;
  @Nullable
  private String loginId;
  @Nullable
  private String sessionId;
  @Nullable
  private String remoteAddr;
  @Nullable
  private String location;
  @Nullable
  private String userAgent;
  @Nullable
  private String exception;
}
