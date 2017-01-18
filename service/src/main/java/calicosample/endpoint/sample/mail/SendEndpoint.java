package calicosample.endpoint.sample.mail;

import javax.inject.Inject;

import calicosample.service.sample.MailService;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;

public class SendEndpoint extends Endpoint<MailService.Form, EmptyOutput> {
  @Inject private MailService mailService;

  @Override
  public EmptyOutput execute(MailService.Form input) {
    mailService.send(input);
    return EmptyOutput.getInstance();
  }
}
