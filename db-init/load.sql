
LOAD DATA INFILE '/docker-entrypoint-initdb.d/dutyfree_sales.csv'
  INTO TABLE duty_free_product
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (year_month, category, count);
