--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // create user authority
-- Migration SQL that makes the change goes here.

CREATE TABLE user_authority (
  id SERIAL NOT NULL,
  user_id INTEGER NOT NULL,
  authority TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by TEXT,
  updated_at TIMESTAMP,
  updated_by TEXT,
  PRIMARY KEY (id)
);

CREATE INDEX user_authority_idx1 ON user_authority (user_id);
CREATE INDEX user_authority_idx2 ON user_authority (authority);

ALTER TABLE user_authority ADD CONSTRAINT user_authority_fk1 FOREIGN KEY (user_id) REFERENCES user_info;


-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE user_authority;

