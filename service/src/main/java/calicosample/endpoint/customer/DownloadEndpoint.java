package calicosample.endpoint.customer;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaStorage;
import jp.co.freemind.calico.media.WithPayload;
import jp.co.freemind.calico.service.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;

public class DownloadEndpoint extends CustomerEndpoint<DownloadEndpoint.Input, DownloadEndpoint.Output> {
  @Inject private MediaStorage mediaStorage;

  @Getter @Setter
  public static class Input {
    @NotNull
    private String id;
  }

  @Getter @Setter
  public static class Output {
    @WithPayload
    private Media photo;
  }

  @Override
  public Output execute(Input input) {
    Output output = new Output();
    Optional<Media> media = mediaStorage.get(input.getId());
    if (!media.isPresent()) {
      throw new ServiceException("id", "存在しないファイルです");
    }
    media.ifPresent(output::setPhoto);
    return output;
  }
}
