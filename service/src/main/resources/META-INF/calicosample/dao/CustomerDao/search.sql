SELECT
 /*%expand "c"*/*
FROM customer c
WHERE
  /*%if @isPresent(form.name)*/
    c.kname1 || ' ' || c.kname2 like /*@infix(form.name) */'a'
  /*%end*/
  /*%if @isPresent(form.sex)*/
    AND c.sex = /*form.sex*/1
  /*%end*/
ORDER BY
  /*%if form._sort.prop == "name"*/
    (c.kname1, c.kname2, c.id)
  /*%elseif form._sort.prop == "sex"*/
    (c.sex, c.id)
  /*%elseif form._sort.prop == "birthday"*/
    (c.birthday, c.id)
  /*%end*/
  /*%if form._sort.isDesc()*/DESC/*%end*/
