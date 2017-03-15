-- // add log column to customer_family
-- Migration SQL that makes the change goes here.

ALTER TABLE customer_family ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE customer_family ADD COLUMN created_by TEXT;
ALTER TABLE customer_family ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE customer_family ADD COLUMN updated_by TEXT;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE customer_family DROP COLUMN created_at;
ALTER TABLE customer_family DROP COLUMN created_by;
ALTER TABLE customer_family DROP COLUMN updated_at;
ALTER TABLE customer_family DROP COLUMN updated_by;
