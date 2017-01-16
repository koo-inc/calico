-- // add log column to customer
-- Migration SQL that makes the change goes here.

ALTER TABLE customer ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE customer ADD COLUMN created_by TEXT;
ALTER TABLE customer ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE customer ADD COLUMN updated_by TEXT;

-- //@UNDO
-- SQL to undo the change goes here.


ALTER TABLE customer DROP COLUMN created_at;
ALTER TABLE customer DROP COLUMN created_by;
ALTER TABLE customer DROP COLUMN updated_at;
ALTER TABLE customer DROP COLUMN updated_by;
