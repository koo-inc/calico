SELECT
  ui.*,
  CASE WHEN count(ua.*) = 0 THEN '[]'::json
  ELSE json_agg(ua.authority)
  END AS authorities
FROM user_info ui
LEFT JOIN user_authority ua
  ON ui.id = ua.user_id
WHERE ui.id = /* id */0
GROUP BY ui.id
