import java.util.List;
import java.util.Properties;

import calicosample.core.CommandInjectorFactory;
import calicosample.core.CommandManager;
import calicosample.core.CommandName;
import jp.co.freemind.calico.config.env.Env;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by kakusuke on 15/07/16.
 */
public class Main {
  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put("injectorFactory", CommandInjectorFactory.class.getName());
    Env.init(properties);

    if (args.length == 0) {
      printUsage();
      System.exit(1);
    }

    String command = args[0];
    String[] attrs = new String[args.length - 1];
    System.arraycopy(args, 1, attrs, 0, args.length - 1);

    try {
      new CommandManager().dispatch(command, attrs);
    }
    catch (Throwable e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.exit(0);
  }

  private static void printUsage() {
    System.out.println(
      "Usage: java -jar <this> <sub command> <arguments>"
    );

    List<CommandName> commandNames = CommandManager.findCommands().stream()
      .map(c -> c.getAnnotation(CommandName.class))
      .collect(toList());

    System.out.println("\nsub commend list:");
    System.out.println(
      commandNames.stream()
        .sorted(comparing(CommandName::id))
        .map(cn -> "\t" + cn.id() + "\t" + cn.description())
        .collect(joining("\n"))
    );
  }
}
