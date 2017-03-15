SELECT
  /*%expand*/*
FROM user_info
WHERE login_id = /*loginId*/0
  AND password = /*password*/'abcdefg'
