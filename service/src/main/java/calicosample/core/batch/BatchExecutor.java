package calicosample.core.batch;

import java.io.ByteArrayOutputStream;

import com.google.inject.Inject;
import jp.co.freemind.calico.context.Context;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.entity.log.BatchEndLog;
import calicosample.entity.log.BatchStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.service.system.LoggingService;
import jp.co.freemind.calico.guice.InjectUtil;
import lombok.Lombok;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Created by kakusuke on 15/07/16.
 */
@Log4j2
public class BatchExecutor<T extends Batch> {
  private final BatchContext batchContext;

  @Inject private Context<CalicoSampleAuthInfo> context;
  @Inject private LoggingService loggingService;

  public static void execute(BatchContext batchContext) {
    new BatchExecutor<>(batchContext).execute();
  }

  public BatchExecutor(@NonNull BatchContext batchContext) {
    this.batchContext = batchContext;
    InjectUtil.injectMembers(this);
  }

  public void execute() {
    ByteArrayOutputStream os = new ByteArrayOutputStream(65535);

    context.switchTo(batchContext, () -> {
      Class<? extends Batch> batchClass = batchContext.getBatchClass();

      long startTime = System.nanoTime();

      BatchStartLog batchStartLog = new BatchStartLog();
      batchStartLog.setClassName(batchClass.getName());
      batchStartLog.setTargetDate(context.getProcessDate());
      batchStartLog.setTargetTime(context.getProcessDateTime().toLocalTime());
      loggingService.insert(batchStartLog);

      BatchLogAppender.attach(os);
      try {
        log.info("バッチ処理 [{}]", batchClass.getSimpleName());

        Batch batch = InjectUtil.injectMembers(batchClass.newInstance());
        batch.run();
      }
      catch (Throwable e) {
        log.catching(e);
        loggingService.insert(ErrorLog.of(batchStartLog, e, null));
        throw Lombok.sneakyThrow(e);
      }
      finally {
        BatchLogAppender.detach();

        BatchEndLog endLog = BatchEndLog.of(batchStartLog);
        endLog.setBody(os.toString());
        loggingService.insert(endLog);

        log.info("バッチ処理時間 [{}] {}s", batchClass.getSimpleName(), (System.nanoTime() - startTime) / 1_000_000 / 1_000.0);
      }
    });
  }
}
