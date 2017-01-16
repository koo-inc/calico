-- // create customer
-- Migration SQL that makes the change goes here.

CREATE TABLE customer (
  id SERIAL NOT NULL,
  kname1 TEXT NOT NULL,
  kname2 TEXT NOT NULL,
  fname1 TEXT,
  fname2 TEXT,
  sex INTEGER,
  favorite_number INTEGER,
  claimer BOOLEAN NOT NULL DEFAULT FALSE,
  birthday DATE,
  contact_enable_start_time TIME,
  contact_enable_end_time TIME,
  PRIMARY KEY(id)
);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE customer;

