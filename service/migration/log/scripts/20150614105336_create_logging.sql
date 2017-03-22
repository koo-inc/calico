-- // create_logging
-- Migration SQL that makes the change goes here.

CREATE UNLOGGED TABLE log (
  id BIGSERIAL,
  ts TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  key TEXT NOT NULL DEFAULT to_char(current_date, 'YYYYMM'),
  version BIGINT,
  PRIMARY KEY (id)
);
CREATE INDEX log_idx1 ON log (key);

CREATE UNLOGGED TABLE access_start_log (
  user_id INTEGER,
  login_id TEXT,
  request_uri TEXT,
  request_method TEXT,
  cookies TEXT,
  params TEXT,
  remote_addr TEXT,
  host_addr TEXT
) INHERITS (log);
CREATE UNIQUE INDEX access_start_log_uniq1 ON access_start_log (id);
CREATE INDEX access_start_log_idx1 ON access_start_log (key);

CREATE UNLOGGED TABLE access_end_log (
  start_log_id BIGINT NOT NULL,
  result_code TEXT,
  interval BIGINT
) INHERITS (log);
CREATE UNIQUE INDEX access_end_log_uniq1 ON access_end_log (id);
CREATE INDEX access_end_log_idx1 ON access_end_log (key);


CREATE UNLOGGED TABLE batch_start_log (
  target_date DATE,
  target_time TIME WITHOUT TIME ZONE,
  class_name TEXT
) INHERITS (log);
CREATE UNIQUE INDEX batch_start_log_uniq1 ON batch_start_log (id);
CREATE INDEX batch_start_log_idx1 ON batch_start_log (key);

CREATE UNLOGGED TABLE batch_end_log (
  start_log_id BIGINT NOT NULL,
  interval BIGINT,
  body TEXT
) INHERITS (log);
CREATE UNIQUE INDEX batch_end_log_uniq1 ON batch_end_log (id);
CREATE INDEX batch_end_log_idx1 ON batch_end_log (key);

CREATE UNLOGGED TABLE error_log (
  start_log_id BIGINT NOT NULL,
  headers TEXT,
  exception TEXT
) INHERITS (log);
CREATE UNIQUE INDEX error_log_uniq1 ON error_log (id);
CREATE INDEX error_log_idx1 ON error_log (key);

CREATE UNLOGGED TABLE js_log (
  user_id INTEGER,
  login_id TEXT,
  session_id TEXT,
  remote_addr TEXT,
  user_agent TEXT,
  location TEXT,
  exception TEXT
) INHERITS (log);
CREATE UNIQUE INDEX js_log_uniq1 ON js_log (id);
CREATE INDEX js_log_idx1 ON js_log (key);

CREATE VIEW access_log AS
SELECT
  s.*,
  e.id AS end_log_id,
  e.result_code,
  e.interval
FROM access_start_log s
LEFT JOIN access_end_log e
  ON s.id = e.start_log_id
  AND s.key = e.key
;

CREATE VIEW access_error_log AS
SELECT
  a.*,
  e.id AS error_log_id,
  e.headers,
  e.exception
FROM access_log a
INNER JOIN error_log e
  ON a.id = e.start_log_id
  AND a.key = e.key
;

CREATE VIEW batch_log AS
SELECT
  s.*,
  e.id AS end_log_id,
  e.interval,
  e.body
FROM batch_start_log s
LEFT JOIN batch_end_log e
  ON s.id = e.start_log_id
  AND s.key = e.key
;

CREATE VIEW batch_error_log AS
SELECT
  a.*,
  e.id AS error_log_id,
  e.exception
FROM batch_log a
INNER JOIN error_log e
  ON a.id = e.start_log_id
  AND a.key = e.key
;

CREATE FUNCTION rotate(tablename TEXT) RETURNS void AS $$
  DECLARE
    key TEXT;
    partition_name TEXT;
  BEGIN
    EXECUTE format('LOCK TABLE ONLY %I IN EXCLUSIVE MODE', tablename);

    FOR key IN EXECUTE format('SELECT DISTINCT key::text FROM ONLY %I', tablename)
    LOOP
      partition_name := tablename || '_' || key;
      IF NOT EXISTS (SELECT true FROM information_schema.tables
        WHERE table_catalog = current_catalog AND table_schema = current_schema AND table_name = partition_name) THEN
        EXECUTE format('CREATE UNLOGGED TABLE %I (CHECK (key = %L))  INHERITS (%I)', partition_name, key, tablename);
        EXECUTE format('CREATE UNIQUE INDEX %I ON %I (id)', partition_name || '_uniq1', partition_name);
      END IF;
      EXECUTE format('INSERT INTO %I SELECT * FROM ONLY %I WHERE key = %L', partition_name, tablename, key);
      EXECUTE format('ANALYZE %I', partition_name);
    END LOOP;

    EXECUTE format('TRUNCATE ONLY %I', tablename);
  END;
$$ LANGUAGE plpgsql;

-- //@UNDO
-- SQL to undo the change goes here.

DROP FUNCTION rotate(TEXT);
DROP TABLE log CASCADE;
