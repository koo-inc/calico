-- // create user_info
-- Migration SQL that makes the change goes here.
CREATE TABLE user_info (
  id SERIAL NOT NULL,
  login_id TEXT NOT NULL,
  password TEXT NOT NULL,
  PRIMARY KEY(id)
);

ALTER TABLE user_info ADD CONSTRAINT user_info_uniq1 UNIQUE (login_id) ;

INSERT INTO user_info (login_id, password) VALUES ('admin', 'admin');


-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE user_info;
