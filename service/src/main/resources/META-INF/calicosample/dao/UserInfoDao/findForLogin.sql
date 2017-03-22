SELECT
  ui.*,
  CASE WHEN count(ua.*) = 0 THEN '[]'::json
  ELSE json_agg(ua.authority)
  END AS authorities
FROM user_info ui
LEFT JOIN user_authority ua
  ON ui.id = ua.user_id
WHERE login_id = /*loginId*/'admin'
  AND password = /*password*/'admin'
GROUP BY ui.id
