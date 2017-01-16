package calicosample.dto.record.customer;

import jp.co.freemind.calico.dto.Record;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.WithPayload;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerPhotoRecord extends Record {
  @WithPayload
  private Media photo;
}
