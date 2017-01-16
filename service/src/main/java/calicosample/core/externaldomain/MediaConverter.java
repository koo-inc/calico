package calicosample.core.externaldomain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaProxy;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.SneakyThrows;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * Created by tasuku on 15/05/08.
 */
@ExternalDomain
public class MediaConverter implements DomainConverter<Media, String>{
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @SneakyThrows
  @Override
  public String fromDomainToValue(Media media) {
    if(media == null) return null;
    return MAPPER.writeValueAsString(media);
  }

  @SneakyThrows
  @Override
  public Media fromValueToDomain(String value) {
    if(value == null) return null;
    Media media = MAPPER.readValue(value, MediaProxy.class);
    Zone.getCurrent().injectMembers(media);
    return media;
  }
}
