package calicosample.core.batch;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Created by kakusuke on 15/07/17.
 */
@Plugin(name="BatchLog", category="Core", elementType="appender", printObject=true)
public class BatchLogAppender extends AbstractAppender {
  private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock readLock = rwLock.readLock();

  private static final ThreadLocal<Deque<OutputStream>> osStack = new ThreadLocal<>();

  public static void attach(OutputStream os) {
    if (osStack.get() == null) {
      osStack.set(new ArrayDeque<>());
    }
    osStack.get().push(os);
  }

  public static void detach() {
    if (osStack.get() == null) return;
    osStack.get().pop();
    if (osStack.get().size() == 0) {
      osStack.remove();
    }
  }

  protected BatchLogAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout, true);
  }

  @Override
  public void append(LogEvent event) {
    if (osStack.get() == null) return;

    try {
      osStack.get().peek().write(readBytes(event));
    }
    catch (IOException e) {
      if (!ignoreExceptions()) {
        throw new AppenderLoggingException(e);
      }
    }
  }

  private byte[] readBytes(LogEvent event) {
    readLock.lock();
    try {
      return getLayout().toByteArray(event);
    }
    finally {
      readLock.unlock();
    }
  }

  @PluginFactory
  public static BatchLogAppender createAppender(
    @PluginAttribute("name") String name,
    @PluginElement("Layout") Layout<? extends Serializable> layout,
    @PluginElement("Filter") final Filter filter) {
    if (name == null) {
      LOGGER.error("No name provided for Redirect");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }

    return new BatchLogAppender(name, filter, layout);
  }
}
