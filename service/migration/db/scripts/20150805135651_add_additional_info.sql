-- // json sample
-- Migration SQL that makes the change goes here.

ALTER TABLE customer ADD COLUMN additional_info_list JSON;
UPDATE customer SET additional_info_list = '[]'::json;
ALTER TABLE customer ALTER COLUMN additional_info_list SET NOT NULL;


-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE customer DROP COLUMN additional_info_list;
