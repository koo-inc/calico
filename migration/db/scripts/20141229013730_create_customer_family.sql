-- // create customer_family
-- Migration SQL that makes the change goes here.

CREATE TABLE customer_family (
  id SERIAL NOT NULL,
  customer_id INTEGER NOT NULL,
  family_type INTEGER,
  name TEXT NOT NULL,
  sex INTEGER,
  favorite_number INTEGER,
  birthday DATE,
  PRIMARY KEY(id)
);

CREATE INDEX customer_family_idx1 ON customer_family(customer_id);

ALTER TABLE customer_family ADD CONSTRAINT customer_family_fk1 FOREIGN KEY (customer_id) REFERENCES customer (id);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE customer_family;
