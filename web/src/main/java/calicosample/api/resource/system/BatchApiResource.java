package calicosample.api.resource.system;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import jp.co.freemind.calico.context.Context;
import calicosample.core.api.ApiResource;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.batch.BatchContext;
import calicosample.core.batch.BatchExecutor;
import calicosample.core.batch.BatchFinder;
import calicosample.dto.form.system.BatchExecForm;
import calicosample.dto.record.system.BatchIndexRecord;
import calicosample.service.system.LoggingService;

import static java.util.stream.Collectors.toList;

/**
 * Created by kakusuke on 15/07/16.
 */
@Path("system/batch")
public class BatchApiResource extends ApiResource {
  @Inject private Context<CalicoSampleAuthInfo> context;
  @Inject private LoggingService loggingService;

  @Path("records")
  @POST
  public List<BatchIndexRecord> records() {
    return BatchFinder.findAll().stream().map(BatchIndexRecord::of).collect(toList());
  }

  @Path("execute")
  @POST
  public void execute(BatchExecForm form) {
    BatchExecutor.execute(
      new BatchContext(context, BatchFinder.find(form.getIndex()), form.getTargetDateTime().orElseGet(context::getProcessDateTime)));
  }
}
