-- // add column to customer
-- Migration SQL that makes the change goes here.

ALTER TABLE customer ADD COLUMN email TEXT;
ALTER TABLE customer ADD COLUMN homepage_url TEXT;
ALTER TABLE customer ADD COLUMN phone_number TEXT;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE customer DROP COLUMN email;
ALTER TABLE customer DROP COLUMN homepage_url;
ALTER TABLE customer DROP COLUMN phone_number;


