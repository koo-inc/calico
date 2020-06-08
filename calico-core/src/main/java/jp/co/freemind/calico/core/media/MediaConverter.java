package jp.co.freemind.calico.core.media;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.freemind.calico.core.di.InjectorRef;
import jp.co.freemind.calico.core.util.Throwables;

/**
 * Created by tasuku on 15/05/08.
 */
@ExternalDomain
public class MediaConverter implements DomainConverter<Media, String>{
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public String fromDomainToValue(Media media) {
    if(media == null) return null;
    try {
      return MAPPER.writeValueAsString(media);
    } catch (Exception e) {
      throw Throwables.sneakyThrow(e);
    }
  }

  @Override
  public Media fromValueToDomain(String value) {
    if(value == null) return null;
    try {
      Media media = MAPPER.readValue(value, MediaProxy.class);
      InjectorRef.injectMembers(media);
      return media;
    } catch (Exception e) {
      throw Throwables.sneakyThrow(e);
    }
  }
}
