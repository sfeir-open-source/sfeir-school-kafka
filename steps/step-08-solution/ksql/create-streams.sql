-- Create Tables and Streams --

-- SET 'auto.offset.reset'='earliest';

CREATE TABLE CUSTOMERS (
  id INTEGER,
  email VARCHAR,
  gender VARCHAR
)
WITH (
  KEY = 'id',
  VALUE_FORMAT = 'json',
  KAFKA_TOPIC  = 'customers'
);

CREATE STREAM ORDERS (
  id VARCHAR,
  customerId INTEGER,
  orderTimestamp BIGINT,
  totalPrice DOUBLE
)
WITH (
  KEY = 'id',
  VALUE_FORMAT = 'json',
  KAFKA_TOPIC  = 'orders'
);

CREATE STREAM CARD_PAYMENTS (
  id VARCHAR,
  paidPrice DOUBLE
)
WITH (
  KEY = 'id',
  VALUE_FORMAT = 'json',
  KAFKA_TOPIC  = 'card_payments'
);

CREATE TABLE SUSPICIOUS_ORDERS AS
  SELECT o.id AS ORDER_ID, COUNT(*) AS PAYMENT_COUNT
  FROM ORDERS o
  INNER JOIN CARD_PAYMENTS p WITHIN 30 MINUTES
  ON p.id = o.id
  GROUP BY o.id
  HAVING COUNT(*) > 1
  EMIT CHANGES;
