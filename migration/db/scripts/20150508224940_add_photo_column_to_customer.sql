-- // add file column to user_info
-- Migration SQL that makes the change goes here.

ALTER TABLE customer ADD COLUMN photo TEXT;


-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE customer DROP COLUMN photo;
