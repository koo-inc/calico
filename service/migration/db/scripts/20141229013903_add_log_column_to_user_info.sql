-- // add log column to user_info
-- Migration SQL that makes the change goes here.

ALTER TABLE user_info ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE user_info ADD COLUMN created_by TEXT;
ALTER TABLE user_info ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE user_info ADD COLUMN updated_by TEXT;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE user_info DROP COLUMN created_at;
ALTER TABLE user_info DROP COLUMN created_by;
ALTER TABLE user_info DROP COLUMN updated_at;
ALTER TABLE user_info DROP COLUMN updated_by;
