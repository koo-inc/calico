package helper.factory
import groovy.sql.Sql

import javax.sql.DataSource

/**
 * Created by tasuku on 15/04/13.
 */
class DataFactory implements GroovyInterceptable {
  private static final Map<String, Table> tableMap = [:]
  private static DataSource ds;

  def static configure(Closure closure) {
    def conf = new Config()
    closure.delegate = conf
    closure.call()
    ds = conf.dataSource
  }

  def static resetAll() {
    tableMap.values().each {
      it.reset()
    }
  }

  def define(String name, Closure builder) {
    return define([id: name, as: name], builder)
  }
  def define(Map param, Closure builder) {
    assert param.id

    def original = tableMap[param.extends]
    assert !param.extends || original

    def name = param.as ?: original?.name ?: param.name
    def builders = [*original?.builders, builder] - null
    def counter = original?.counter ?: new Counter()
    tableMap[param.id] = new Table(name: name, builders: builders, counter: counter)
    return tableMap[param.id]
  }

  static def create(String name) {
    return createInner(name, ({}), [:], null)
  }
  static def create(String name, Map values) {
    return createInner(name, ({}), values, null)
  }

  static def create(String name, Map values, Closure after) {
    return createInner(name, ({}), values, after)
  }

  private static def stash
  private static def createCount = 0
  private static def createInner(name, closure, values, after) {
    createCount++
    stash = stash?: [:] as Expando

    assert tableMap[name] != null
    Table table = tableMap[name]

    def _props = buildProps([*table.builders, closure], values, table.counter)
    def props = [:]
    def types = [:]
    _props.entrySet().each {
      if (it.key.contains('$')) {
        def ref = it.key.split('\\$')
        props.put(ref[0], it.value)
        types.putAt(ref[0], ref[1])
      }
      else {
        props.put(it.key, it.value)
      }
    }

    def result = new Sql(ds).rows("""
      INSERT INTO ${table.name}
      (${props.keySet().join(',')})
      VALUES
      (${props.keySet().collect { types.containsKey(it) ? "cast(:" + it + " as " + types[it] + ")" : ":" + it }.join(',')})
      RETURNING *
    """, props)[0] as Map

    resetSequence(table.name)

    def delegate = result as Expando
    delegate.$stash = stash

    after = (after?: table.after)?.rehydrate(delegate, table.after?.owner, table.after?.thisObject)
    after?.call(delegate)

    createCount--
    if (createCount <= 0) {
      stash = null
    }

    return result
  }

  private static buildProps(builders, values, counter) {
    def row = buildRow(builders, counter)
    def props = [:]
    values.entrySet().each {
      props[it.key] = evaluate(values.get(it.key))
    }

    def keys = props.keySet().collect { key -> normalizeKey(key) }
    row.entrySet().each {
      if (!keys.contains(normalizeKey(it.key))) {
        props[it.key] = evaluate(it.value)
      }
    }
    return props
  }

  private static String normalizeKey(String key) {
    return key.contains('$') ? key.substring(0, key.indexOf('$')) : key
  }

  private static buildRow(builders, counter) {
    counter++

    def row = [:]
    builders.each {
      def context = [:] as Expando
      it.delegate = context
      it.resolveStrategy = Closure.DELEGATE_FIRST
      it(counter.count)
      row.putAll(context.properties)
    }
    return row
  }

  private static evaluate(value) {
    if (value instanceof Closure) {
      def c = (Closure) value
      c.delegate = [:] as Expando
      c.delegate.$stash = stash
      c.resolveStrategy = Closure.DELEGATE_FIRST
      return evaluate(c())
    }
    if (value instanceof GString) {
      return value.toString()
    }
    return value
  }

  private static resetSequence(tableName) {
    new Sql(ds).call("""
    DO \$\$ DECLARE
      r record;
      r2 record;
    BEGIN
      FOR r IN SELECT table_name, column_name, substr(column_default, 10, length(column_default) - 21) AS sequence
               FROM information_schema.columns
               WHERE column_default LIKE 'nextval(%'
                 AND table_catalog = current_database()
                 AND table_schema = current_schema()
                 AND table_name = '""" + tableName + """'
      LOOP
        FOR r2 IN EXECUTE 'SELECT COALESCE(MAX(' || quote_ident(r.column_name) || '), 0) + 1 AS seq FROM ' || quote_ident(r.table_name)
        LOOP
          EXECUTE 'ALTER SEQUENCE ' || quote_ident(r.sequence) || ' RESTART ' || r2.seq;
        END LOOP;
      END LOOP;
    END\$\$;
    """)
  }

  private static class Table {
    String name
    List<Closure> builders = []
    Counter counter
    Closure after;

    def void reset() {
      counter.reset()
    }

    def Table leftShift(Closure closure) {
      after = closure
      return this;
    }
  }

  private static class Counter {
    int count = 0
    def next() {
      count ++
      this
    }
    def reset() {
      count = 0
    }
  }

  private static class Config {
    private def DataSource dataSource
    def void resister(Class clazz) {
      clazz.newInstance()
    }

    def void dataSource(DataSource ds) {
      this.dataSource = ds
    }
  }
}
