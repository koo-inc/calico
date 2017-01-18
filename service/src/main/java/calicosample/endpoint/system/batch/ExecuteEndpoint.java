package calicosample.endpoint.system.batch;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.batch.BatchContext;
import calicosample.core.batch.BatchExecutor;
import calicosample.core.batch.BatchFinder;
import com.google.inject.Inject;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.endpoint.dto.EmptyOutput;
import lombok.Getter;
import lombok.Setter;

public class ExecuteEndpoint extends Endpoint<ExecuteEndpoint.Input, EmptyOutput> {
  @Inject private Context<CalicoSampleAuthInfo> context;

  @Getter @Setter
  public static class Input {
    @NotNull
    private String index;
    private Optional<LocalDateTime> targetDateTime;
  }

  @Override
  public EmptyOutput execute(Input input) {
    BatchExecutor.execute(
      new BatchContext(context, BatchFinder.find(input.getIndex()), input.getTargetDateTime().orElseGet(context::getProcessDateTime)));
    return EmptyOutput.getInstance();
  }
}
