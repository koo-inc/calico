package calicosample.endpoint.sample.mail;

import java.util.Optional;

import calicosample.service.sample.MailService;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyInput;

public class FormEndpoint extends Endpoint<EmptyInput, MailService.Form> {

  @Override
  public MailService.Form execute(EmptyInput input) {
    return new MailService.Form(){{
      setFromAddress("admin@freemind.co.jp");
      setFromName(Optional.of("空管理者"));
      setSubject("テストメール");
      setBody("テスト\nあいうえお");
    }};
  }
}
