package calicosample.core;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import calicosample.core.auth.CalicoSampleAuthInfo;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.context.Context;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.util.ClassFinder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static java.util.stream.Collectors.toList;

/**
 * Created by kakusuke on 15/07/16.
 */
@Log4j2
public class CommandManager {
  @Inject @Named("sessionScope") SimpleScope sessionScope;
  @Inject @Named("requestScope") SimpleScope requestScope;
  public CommandManager() {
    InjectUtil.injectMembers(this);
  }

  @SneakyThrows
  public void dispatch(String command, String[] args) {
    Class<? extends Command> commandClass = find(command);
    log.info("command " + command + ", implemented " + commandClass.getName());
    // RequestScoped, SessionScoped をエミュレート
    sessionScope.enter();
    requestScope.enter();
    InjectUtil.getInjector()
      .getInstance(Key.get(new TypeLiteral<Context<CalicoSampleAuthInfo>>() {}))
      .switchTo(new CommandContext());
    try {
      commandClass.newInstance().accept(args);
    }
    finally {
      sessionScope.exit();
      requestScope.exit();
    }
  }

  @SneakyThrows
  public Class<? extends Command> find(String command) {
    return findCommands().stream()
      .filter(c -> c.getAnnotation(CommandName.class).id().equals(command))
      .findFirst()
      .orElseThrow(() -> new ClassNotFoundException("command " + command + " is not defined"));
  }

  @SuppressWarnings("unchecked")
  public static List<Class<? extends Command>> findCommands() {
    return ClassFinder.findClasses(Env.getRootPackage() + ".command").stream()
      .filter(Command.class::isAssignableFrom)
      .filter(c -> c.isAnnotationPresent(CommandName.class))
      .map(c -> (Class<? extends Command>) c)
      .collect(toList());
  }
}
