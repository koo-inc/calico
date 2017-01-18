package calicosample.service.sample;

import java.util.Optional;

import javax.inject.Inject;

import jp.co.freemind.calico.mail.Mail;
import jp.co.freemind.calico.mail.Mailer;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.service.Service;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

public class MailService extends Service {
  @Inject Mailer mailer;

  @Getter @Setter
  public static class Form {
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

  public void send(Form form) {
    Mail.Builder builder = Mail.builder()
      .to(form.getToAddress())
      .from(form.getFromAddress())
      .subject(form.getSubject())
      .body(form.getBody());
    form.getFromName().ifPresent(v -> builder.from(form.getFromAddress(), v));
    form.getReplyTo().ifPresent(builder::replyTo);
    form.getAttachment().ifPresent(builder::attach);

    mailer.getPostMan().deliver(builder.build());
  }
}
