package calicosample.entity.log;

import org.seasar.doma.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakusuke on 15/06/14.
 */
@Entity
@Getter @Setter
public class AccessStartLog extends LogEntity implements StartLog {
  private Integer userId;
  private String loginId;
  private String requestUri;
  private String requestMethod;
  private String cookies;
  private String params;
  private String remoteAddr;
  private String hostAddr;
}
