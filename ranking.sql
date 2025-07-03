SELECT
  category,
  SUM(sales_count) AS total
FROM duty_free_product
WHERE DATE_FORMAT(
        STR_TO_DATE(`year_month`, '%b.%y'),
        '%Y-%m'
      ) = '2019-01'
GROUP BY category
ORDER BY total DESC
LIMIT 10;
