SELECT
  ui.*,
  CASE WHEN count(ur.*) = 0 THEN '[]'::json
  ELSE json_agg(ur.right)
  END AS rights
FROM user_info ui
LEFT JOIN user_right ur
  ON ui.id = ur.user_id
WHERE login_id = /*loginId*/'admin'
GROUP BY ui.id
