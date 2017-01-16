package calicosample.endpoint.sample.mail;

import calicosample.service.sample.MailService;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;

public class FormEndpoint implements Endpoint<EmptyInput, MailService.Form> {

  @Override
  public MailService.Form execute(EmptyInput input) {
    return new MailService.Form(){{
      setFromAddress("admin@freemind.co.jp");
      setFromName("空管理者");
      setSubject("テストメール");
      setBody("テスト\nあいうえお");
    }};
  }
}
