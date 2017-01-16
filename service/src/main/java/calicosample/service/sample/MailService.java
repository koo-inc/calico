package calicosample.service.sample;

import javax.annotation.Nullable;
import javax.inject.Inject;

import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.mail.Mail;
import jp.co.freemind.calico.mail.Mailer;
import lombok.Getter;
import lombok.Setter;

public class MailService {
  @Inject Mailer mailer;

  @Getter @Setter
  public static class Form {
    private String toAddress;
    private String fromAddress;
    @Nullable
    private String fromName;
    private String replyTo;
    private String subject;
    private String body;
    @Nullable
    private Media attachment;
  }

  public void send(Form form) {
    Mail.Builder builder = Mail.builder()
      .to(form.getToAddress())
      .from(form.getFromAddress())
      .subject(form.getSubject())
      .body(form.getBody());

    if (form.getFromName() != null) {
      builder.from(form.getFromAddress(), form.getFromName());
    }
    if (form.getReplyTo() != null) {
      builder.replyTo(form.getReplyTo());
    }
    if (form.getAttachment() != null) {
      builder.attach(form.getAttachment());
    }

    mailer.getPostMan().deliver(builder.build());
  }
}
