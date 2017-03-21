package calicosample.endpoint.userinfo;

import calicosample.Messages;
import calicosample.extenum.CalicoSampleRight;
import jp.co.freemind.calico.core.auth.Authorizable;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.exception.AuthorizationException;
import jp.co.freemind.calico.core.zone.Context;

public class BaseAuthZ implements Authorizable {
  @Override
  public void authorize(Context context, EndpointInvocation invocation) throws AuthorizationException {
    if (!hasRights(context, CalicoSampleRight.USER_INFO_READ)) {
      throw new AuthorizationException(Messages.NOT_AUTHORIZED);
    }
  }
}
