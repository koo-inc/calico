package jp.co.freemind.calico.core.zone

import com.google.inject.*
import com.google.inject.name.Names
import groovy.transform.EqualsAndHashCode
import jp.co.freemind.calico.core.endpoint.TransactionScoped
import spock.lang.Specification

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ZoneTest extends Specification {
  static Zone root
  def setupSpec() {
    root = Zone.initialize({spec ->
      spec.modules(new Module() {
        @Override
        void configure(Binder binder) {
          binder.bind(String).annotatedWith(Names.named("test1")).toInstance("root")
          binder.bind(Foo).toProvider(new Provider() {
            Object get() { return new Foo(name: 'root') }
          }).in(TransactionScoped)
          binder.bindScope(TransactionScoped, new ZoneScope(TransactionScoped))
        }
      })
    })
  }

  def cleanupSpec() {
    Zone.dispose()
  }

  def "forkしたとき新しいInjectorのスコープが生まれる"() {
    when:
    def child = Zone.current.fork({spec -> spec.modules(new Module() {
      @Override
      void configure(Binder binder) {
        binder.bind(String).annotatedWith(Names.named("test2")).toInstance("child")
      }
    })})

    then:
    assert root.getInstance(Key.get(String.class, Names.named("test1"))) == "root"
    assert Zone.current.getInstance(Key.get(String.class, Names.named("test1"))) == "root"
    child.run {->
      assert child.getInstance(Key.get(String.class, Names.named("test1"))) == "root"
      assert child.getInstance(Key.get(String.class, Names.named("test2"))) == "child"
      assert Zone.current.getInstance(Key.get(String.class, Names.named("test1"))) == "root"
      assert Zone.current.getInstance(Key.get(String.class, Names.named("test2"))) == "child"
    }

    when:
    root.getInstance(Key.get(String.class, Names.named("test2")))
    then:
    thrown(ConfigurationException)

    when:
    Zone.current.getInstance(Key.get(String.class, Names.named("test2")))
    then:
    thrown(ConfigurationException)

  }

  def "forkしたときプロバイダが設定したものになる"() {
    when:
    def child = Zone.current.fork({s -> s
      .scope(TransactionScoped)
      .provide(new Foo(name: 'child'))
    })
    def grandChild = child.fork({s -> s
      .scope(TransactionScoped)
      .provide(new Foo(name: 'grand child'))
    })

    then:
    assert Zone.current.getInstance(Foo).name == "root"
    child.run {->
      assert Zone.current.getInstance(Foo).name == "child"
      grandChild.run {->
        assert Zone.current.getInstance(Foo).name == "grand child"
      }
      assert Zone.current.getInstance(Foo).name == "child"
    }
    assert Zone.current.getInstance(Foo).name == "root"
  }

  def "forkしたときスコープを設定していなければ親を見る"() {
    when:
    def child = Zone.current.fork({s -> s
      .scope(TransactionScoped)
      .provide(new Foo(name: 'child'))
    })
    def grandChild = child.fork({s -> s})

    then:
    assert Zone.current.getInstance(Foo).name == "root"
    child.run {->
      assert Zone.current.getInstance(Foo).name == "child"
      grandChild.run {->
        assert Zone.current.getInstance(Foo).name == "child"
      }
      assert Zone.current.getInstance(Foo).name == "child"
    }
    assert Zone.current.getInstance(Foo).name == "root"
  }

  def "forkしたときスコープを設定していても子になければ親を見る"() {
    when:
    def child = Zone.current.fork({s -> s
      .scope(TransactionScoped)
      .provide(new Foo(name: 'child'))
    })
    def grandChild = child.fork({s -> s.scope(TransactionScoped)})

    then:
    assert Zone.current.getInstance(Foo).name == "root"
    child.run {->
      assert Zone.current.getInstance(Foo).name == "child"
      grandChild.run {->
        assert Zone.current.getInstance(Foo).name == "child"
      }
      assert Zone.current.getInstance(Foo).name == "child"
    }
    assert Zone.current.getInstance(Foo).name == "root"
  }

  def "子で例外が起きたときは子で処理できる"() {
    when:
    def error
    root.fork({spec -> spec.onError({e -> error = e}) }).run() {
      throw new RuntimeException("foo")
    }

    then:
    notThrown(Throwable)
    assert error.message == 'foo'
  }

  def "子で例外が起きたとき、子が処理しなければ親で処理できる"() {
    when:
    def error
    root.fork({s -> s.onError({e -> error = "parent"})})
      .fork({s -> s}).run() {
      throw new RuntimeException("child")
    }

    then:
    notThrown(Throwable)
    assert error == "parent"
  }

  def "子で例外が起きたとき、親が処理しなければ UnhandledException 発生"() {
    when:
    root.fork({s -> s}).run() {
      throw new RuntimeException("error")
    }

    then:
    def e = thrown(UnhandledException)
    assert e.message == "error"
  }

  def "子の例外処理で例外が発生した場合、親で処理できる"() {
    when:
    def error
    root.fork({s -> s.onError({e -> error = "parent"})})
      .fork({s -> s.onError({e -> throw new RuntimeException('child') }) }).run() {
      throw new RuntimeException("error")
    }

    then:
    notThrown(Throwable)
    assert error == 'parent'
  }

  def "例外処理内で発生した例外を誰も処理しなかった場合、UnhandledException 発生"() {
    when:
    def error
    root.fork({s -> s})
      .fork({s -> s.onError({e -> throw new RuntimeException('child') }) }).run() {
      throw new RuntimeException("error")
    }

    then:
    def e = thrown(UnhandledException)
    assert e.message == 'child'
  }

  def "例外処理中は該当の Zone が currentZone に"() {
    when:
    def contexts = []
    def parent = root.fork({s -> s.onError({e -> contexts << Zone.current})})
    parent.run() {
      throw new RuntimeException("parent")
    }
    parent.fork({s -> s}).run() {
      throw new RuntimeException("child")
    }

    then:
    assert contexts[0] == parent
    assert contexts[1] == parent
  }

  def "スコープを抜けるときに終了処理がよばれる"() {
    when:
    def process = []
    try {
      root.fork({s -> s
        .onFinish({->
          process << 1
        })
        .onError({e ->
          process << 2
          throw e
        })
      }).run({->
        Zone.current.fork({s -> s})
          .run({->
            Zone.current.fork({s -> s
              .onFinish({->
              process << 3
            })
            .onError({e ->
              process << 4
              throw e
            })
          }).run({-> throw new RuntimeException()})
        })
      })
    } catch (Exception ignored) {}

    then:
    assert process == [4, 3, 2, 1]
  }

  def "別スレッドでも currentZone が正しく設定される"() {
    when:
    def zone = root.fork({s -> s})
    def exec = Executors.newFixedThreadPool(1)
    def actual
    exec.submit(zone.bind({-> actual = Zone.current }))
    exec.awaitTermination(1, TimeUnit.SECONDS)

    then:
    assert actual == zone
  }

  def "別スレッドでも例外が伝搬される"() {
    when:
    def error
    def zone = root.fork({s -> s.onError({e -> error = e})})
    def exec = Executors.newFixedThreadPool(1)
    exec.submit(zone.bind({-> throw new ChildException() }))
    exec.awaitTermination(1, TimeUnit.SECONDS)

    then:
    assert error instanceof ChildException
  }

  static class RootException extends RuntimeException {
    RootException(Throwable e) { super(e) }
  }
  static class ChildException extends RuntimeException {
    ChildException() { super() }
    ChildException(Throwable e) { super(e) }
  }

  @EqualsAndHashCode
  static class Foo {
    String name
  }
}

@Retention(RetentionPolicy.RUNTIME)
@interface ParentScope {}

@Retention(RetentionPolicy.RUNTIME)
@interface ChildScope {}
