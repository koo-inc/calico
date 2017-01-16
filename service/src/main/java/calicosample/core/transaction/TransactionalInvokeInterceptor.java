package calicosample.core.transaction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionAttribute;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;
import org.seasar.doma.jdbc.tx.TransactionManager;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import jp.co.freemind.calico.guice.InjectUtil;
import jp.co.freemind.calico.util.StreamUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/**
 * Created by tasuku on 15/03/11.
 */
@Log4j2
public class TransactionalInvokeInterceptor implements MethodInterceptor {

  private static final CallStack callStack = new CallStack();

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      callStack.push(invocation);

      if (isExposedMethodInvocation(invocation) && calledWithinServiceMethod()) {
        throw new IllegalAccessException(message("他のトランザクション境界メソッド実行中にトランザクション境界メソッドが呼び出されました"));
      }

      if (!isExposedMethodInvocation(invocation) && !calledWithinServiceMethod()) {
        throw new IllegalAccessException(message("トランザクション境界メソッドでないメソッドが外部から呼び出されました"));
      }

      Optional<TransactionAttribute> attr = getTransactionAttribute(invocation.getMethod());
      Optional<TransactionIsolationLevel> level = getTransactionIsolationLevel(invocation.getMethod());
      if (!attr.isPresent() || !level.isPresent()) {
        return invocation.proceed();
      }

      TransactionManager tm = InjectUtil.getInstance(Config.class).getTransactionManager();
      switch (attr.get()) {
        case NOT_SUPPORTED:
          return tm.notSupported(level.get(), () -> proceed(invocation));
        case REQURES_NEW:
          log.debug("トランザクション開始要求=[属性: {} 分離レベル: {}]", attr.get(), level.get());
          return tm.requiresNew(level.get(), () -> proceed(invocation));
        case REQURED:
          log.debug("トランザクション開始要求=[属性: {} 分離レベル: {}]", attr.get(), level.get());
          return tm.required(level.get(), () -> proceed(invocation));
      }
      throw new IllegalStateException(message("トランザクション属性 {} はサポート外です", attr.get()));
    }
    finally {
      callStack.pop();
    }
  }

  private String message(String template, Object... args) {
    ImmutableList.Builder<Object> params = ImmutableList.builder().add(args);
    StreamUtil.of(callStack)
      .map(MethodInvocation::getMethod)
      .forEach(params::add);

    return log.getMessageFactory()
      .newMessage(template + Strings.repeat("\n\t{}", callStack.size()), params.build().toArray())
      .getFormattedMessage();
  }

  private boolean isExposedMethodInvocation(MethodInvocation invocation) {
    Method method = invocation.getMethod();
    int modifier = method.getModifiers();
    // 静的メソッドは対象外にする (このインターセプタが挟まれない可能性がある)
    if (Modifier.isStatic(modifier)) return false;
    // public メソッドだけを対象とする
    if (!Modifier.isPublic(modifier)) return false;

    // 明示的にアノテーションがついているものが外から呼び出されるメソッド
    if (method.isAnnotationPresent(Read.class)) return true;
    if (method.isAnnotationPresent(Write.class)) return true;
    if (method.isAnnotationPresent(NoTransaction.class)) return true;
    return false;
  }

  private boolean calledWithinServiceMethod() {
    return callStack.size() > 1;
  }

  private Optional<TransactionAttribute> getTransactionAttribute(Method method) {
    if (method.isAnnotationPresent(NoTransaction.class)) return Optional.of(TransactionAttribute.NOT_SUPPORTED);
    if (method.isAnnotationPresent(Write.class)) return Optional.of(TransactionAttribute.REQURED);
    if (method.isAnnotationPresent(Read.class)) return Optional.of(TransactionAttribute.REQURED);
    return Optional.empty();
  }
  private Optional<TransactionIsolationLevel> getTransactionIsolationLevel(Method method) {
    if (method.isAnnotationPresent(NoTransaction.class)) return Optional.of(TransactionIsolationLevel.DEFAULT);
    if (method.isAnnotationPresent(Write.class)) return Optional.of(TransactionIsolationLevel.SERIALIZABLE);
    if (method.isAnnotationPresent(Read.class)) return Optional.of(TransactionIsolationLevel.READ_COMMITTED);
    return Optional.empty();
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private <T> T proceed(MethodInvocation invocation) {
    T result = (T) invocation.proceed();
     return result;
  }

  private static class CallStack implements Iterable<MethodInvocation> {
    ThreadLocal<Deque<MethodInvocation>> tlCallStack = new ThreadLocal<>();

    public void push(MethodInvocation invocation) {
      log.trace("TransactionInvokeInterceptor invoke start: {}", invocation.getMethod());
      getCallStack().push(invocation);
    }
    public MethodInvocation pop() {
      Deque<MethodInvocation> callStack = getCallStack();
      MethodInvocation invocation = callStack.pop();
      log.trace("TransactionInvokeInterceptor invoke end: {}, stack size: {}", invocation.getMethod(), callStack.size());
      if (callStack.size() == 0) {
        tlCallStack.remove();
      }
      return invocation;
    }

    @Override
    public Iterator<MethodInvocation> iterator() {
      if (tlCallStack.get() == null) return Collections.emptyIterator();
      return tlCallStack.get().iterator();
    }

    public int size() {
      if (tlCallStack.get() == null) return 0;
      return tlCallStack.get().size();
    }

    private Deque<MethodInvocation> getCallStack() {
      Deque<MethodInvocation> callStack = tlCallStack.get();
      if (callStack == null) {
        callStack = new ArrayDeque<>();
        tlCallStack.set(callStack);
      }
      return callStack;
    }
  }
}
