package jp.co.freemind.calico.core.endpoint.interceptor;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.result.Result;
import jp.co.freemind.calico.core.endpoint.result.ResultType;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;

public class ResultMapper implements EndpointInterceptor {

  @Override
  @SuppressWarnings("rawtype")
  public Object invoke(EndpointInvocation invocation) throws Throwable {
    Object output = invocation.proceed();

    Result result;
    if (output instanceof Result) {
      result = (Result) output;
    }
    else {
      result = new Result(ResultType.JSON, Zone.getCurrent().getInstance(Context.class), output);
    }

    return result;
  }

}
