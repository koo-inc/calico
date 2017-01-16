package calicosample.dto.form.sample.mail;

import java.util.Optional;

import jp.co.freemind.calico.dto.Form;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter @Setter
public class MailForm extends Form {
  @NotEmpty
  private String toAddress;
  @NotEmpty
  private String fromAddress;
  private Optional<String> fromName;
  private Optional<String> replyTo;
  @NotEmpty
  private String subject;
  @NotEmpty
  private String body;
  private Optional<Media> attachment;
}
