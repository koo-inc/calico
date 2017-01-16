package calicosample.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import com.google.inject.Inject;
import calicosample.core.Command;
import calicosample.core.CommandName;
import jp.co.freemind.calico.context.Context;
import calicosample.core.auth.CalicoSampleAuthInfo;
import calicosample.core.batch.BatchContext;
import calicosample.core.batch.BatchExecutor;
import calicosample.core.batch.BatchFinder;
import calicosample.core.batch.BatchIndex;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by kakusuke on 15/07/16.
 */
@CommandName(id = "batch:exec", description = "指定したバッチを実行します")
public class BatchExecCommand implements Command {
  @Inject Context<CalicoSampleAuthInfo> context;

  @Override
  public void accept(String[] args) {
    LocalDateTime targetDateTime = getTargetDateTime();

    List<BatchContext> contexts = Stream.of(args)
      .map(BatchFinder::find)
      .map(c -> new BatchContext(context, c, targetDateTime))
      .collect(toList());

    if (contexts.size() == 0) {
      printUsage();
      return;
    }

    contexts.forEach(BatchExecutor::execute);
  }

  private LocalDateTime getTargetDateTime() {
    if (System.getProperty("targetDateTime") == null) {
      return LocalDateTime.now();
    }
    else {
      return LocalDateTime.parse(System.getProperty("targetDateTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
  }

  private void printUsage() {
    System.out.println("Usage: java -jar <this> batch:exec <batch name>");
    System.out.println("\nbatch name list:");
    System.out.println(
      BatchFinder.findAll().stream()
        .map(c -> c.getAnnotation(BatchIndex.class))
        .sorted(comparing(BatchIndex::index))
        .map(bi -> "\t" + bi.index() + "\t" + bi.label() + "\t" + bi.description())
        .collect(joining("\n"))
    );
  }
}
