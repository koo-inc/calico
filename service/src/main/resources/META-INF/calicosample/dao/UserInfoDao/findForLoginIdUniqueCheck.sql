SELECT
  /*%expand*/*
FROM user_info
WHERE login_id = /*loginId*/0
  /*%if @isPresent(exceptId)*/
  AND id != /*exceptId*/0
  /*%end*/
