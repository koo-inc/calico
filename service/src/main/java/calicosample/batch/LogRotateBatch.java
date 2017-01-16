package calicosample.batch;

import java.util.stream.Stream;

import com.google.inject.Inject;
import calicosample.core.batch.Batch;
import calicosample.core.batch.BatchIndex;
import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.BatchEndLog;
import calicosample.entity.log.BatchStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.entity.log.JsLog;
import calicosample.service.system.LoggingService;
import lombok.extern.log4j.Log4j2;

/**
 * Created by kakusuke on 15/07/16.
 */
@BatchIndex(index = "log:rotate", label = "ログローテーション",
  description = "検索速度向上のためにログのメンテナンスを行います。")
@Log4j2
public class LogRotateBatch implements Batch {
  @Inject
  LoggingService service;

  @Override
  public void run() {
    Stream.of(
      AccessStartLog.class,
      AccessEndLog.class,
      BatchStartLog.class,
      BatchEndLog.class,
      ErrorLog.class,
      JsLog.class
    ).forEach(c ->
      measure(c.getSimpleName(), ()-> service.rotate(c)));
  }

  private void measure(String mark, Runnable runnable) {
    long start = System.nanoTime();
    runnable.run();
    log.info("elapsed time {}ms [{}]", String.format("%.03f", (System.nanoTime() - start) / 1_000_000.0), mark);
  }
}
