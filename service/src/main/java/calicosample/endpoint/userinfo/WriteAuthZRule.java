package calicosample.endpoint.userinfo;

import calicosample.Messages;
import calicosample.extenum.CalicoSampleAuthority;
import jp.co.freemind.calico.core.auth.AuthorizationRule;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Context;

public class WriteAuthZRule implements AuthorizationRule {
  @Override
  public void apply(Context context, Class<? extends Endpoint<?, ?>> endpointClass, Object input) throws AuthorizationException {
    if (!isAuthorizedWith(context, CalicoSampleAuthority.USER_INFO_WRITE)) {
      throw new AuthorizationException(Messages.NOT_AUTHORIZED);
    }
  }
}
