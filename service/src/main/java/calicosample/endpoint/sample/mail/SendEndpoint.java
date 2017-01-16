package calicosample.endpoint.sample.mail;

import javax.inject.Inject;

import calicosample.service.sample.MailService;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;

public class SendEndpoint implements Endpoint<MailService.Form, EmptyOutput> {
  @Inject private MailService mailService;

  @Override
  public EmptyOutput execute(MailService.Form input) {
    mailService.send(input);
    return EmptyOutput.getInstance();
  }
}
