package calicosample.api.resource.sample;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.core.auth.NoAuth;
import calicosample.dto.form.sample.mail.MailForm;
import calicosample.service.sample.MailService;

@Path("sample/mail")
@NoAuth
public class MailApiResource extends ApiResource {
  @Inject
  MailService mailService;

  @Path("form")
  @POST
  public MailForm form() {
    return mailService.getForm();
  }

  @Path("send")
  @POST
  public void send(MailForm form) {
    mailService.send(form);
  }

}
