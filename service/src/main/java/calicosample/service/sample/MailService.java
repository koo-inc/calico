package calicosample.service.sample;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import calicosample.dto.form.sample.mail.MailForm;
import jp.co.freemind.calico.mail.Mail;
import jp.co.freemind.calico.mail.Mailer;
import jp.co.freemind.calico.service.Service;

public class MailService extends Service {
  @Inject
  Mailer mailer;

  public MailForm getForm(){
    return new MailForm(){{
      setFromAddress("admin@freemind.co.jp");
      setFromName(Optional.of("空管理者"));
      setSubject("テストメール");
      setBody("テスト\nあいうえお");
    }};
  }

  public void send(@Valid MailForm form) {
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
