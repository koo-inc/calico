SELECT
  /*%expand*/*
FROM customer c
--LEFT OUTER JOIN customer_family f
ORDER BY c.fname1, c.fname2, c.id
