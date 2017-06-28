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
public class AccessStartLog extends LogEntity implements StartLog {
  @Nullable
  private String loginId;
  @Nullable
  private String requestUri;
  @Nullable
  private String refererPath;
  @Nullable
  private String cookies;
  @Nullable
  private String params;
  @Nullable
  private String remoteAddr;
  @Nullable
  private String hostAddr;
}
