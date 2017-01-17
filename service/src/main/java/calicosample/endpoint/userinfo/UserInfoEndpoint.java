package calicosample.endpoint.userinfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.co.freemind.calico.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

public abstract class UserInfoEndpoint<INPUT, OUTPUT> extends Endpoint<INPUT, OUTPUT> {

  @Getter @Setter
  public static class IdInput {
    @NotNull
    private Integer id;
  }

  @Getter @Setter
  public static abstract class CommonFormInput {
    @NotEmpty
    @Size(min = 3, max = 10)
    private String loginId;

    @NotEmpty
    private String password;
  }
}
