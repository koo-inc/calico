package jp.co.freemind.calico.core.endpoint.interceptor;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInterceptor;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.dto.ContextualOutput;
import jp.co.freemind.calico.core.endpoint.interceptor.result.Result;
import jp.co.freemind.calico.core.endpoint.interceptor.result.ResultType;
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
    else if (output instanceof ContextualOutput) {
      ContextualOutput contextualOutput = (ContextualOutput) output;
      result = new Result(ResultType.JSON, contextualOutput.getContext(), contextualOutput.getOutput());
    }
    else {
      result = new Result(ResultType.JSON, Zone.getCurrent().getInstance(Context.class), output);
    }

    return result;
  }

}
