package calicosample.endpoint.system.batch;

import java.util.stream.Stream;

import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.BatchEndLog;
import calicosample.entity.log.BatchStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.entity.log.JsLog;
import calicosample.service.system.LoggingService;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.dto.EmptyInput;
import jp.co.freemind.calico.core.endpoint.dto.EmptyOutput;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogRotateEndpoint implements Endpoint<EmptyInput, EmptyOutput> {
  @Inject
  private LoggingService service;

  @Override
  public EmptyOutput execute(EmptyInput emptyInput) {
    Stream.of(
      AccessStartLog.class,
      AccessEndLog.class,
      BatchStartLog.class,
      BatchEndLog.class,
      ErrorLog.class,
      JsLog.class
    ).forEach(c ->
      measure(c.getSimpleName(), ()-> service.rotate(c)));

    return EmptyOutput.getInstance();
  }

  private void measure(String mark, Runnable runnable) {
    long start = System.nanoTime();
    runnable.run();
    log.info("elapsed time {}ms [{}]", String.format("%.03f", (System.nanoTime() - start) / 1_000_000.0), mark);
  }
}
