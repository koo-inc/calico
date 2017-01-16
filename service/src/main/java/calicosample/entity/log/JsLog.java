package calicosample.entity.log;

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
  private String loginId;
  private String sessionId;
  private String remoteAddr;
  private String location;
  private String userAgent;
  private String exception;
}
