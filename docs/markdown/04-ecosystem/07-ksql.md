<!-- .slide: class="transition" -->

# ksqlDB

##==##

<!-- .slide: -->

# ksqlDB

![h-200](./assets/images/ksql-logo.png)

* Moteur de streaming basé sur une syntaxe SQL
* Abstraction au dessus de Kafka Streams
* Adapté pour les non développeurs

##==##
<!-- .slide: -->

# Architecture

![h-800 center](./assets/images/ksqldb-architecture.svg)

##==##
<!-- .slide: -->

# Serveur ksqlDB

* Instance de traitement des requêtes ksqlDB
* Expose une interface REST pour exécuter des requêtes
* Configuration via le fichier `/etc/ksql/ksql-server.properties`:
  * `bootstrap.servers`
  * `listeners`
  * `ksql.schema.registry.url`

##==##
<!-- .slide: class="with-code" -->

# Serveur ksqlDB (suite)

**Démarrer une instance**

```bash
$ ksql-server-start /etc/ksql/ksql-server.properties
```

<!-- .element: class="big-code" -->

<br>

**Vérifier l'état de santé**

```bash
$ http GET http://localhost:8088/info
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Client ksqlDB

```bash
$ ksql http://localhost:8088

>   ===========================================
>   ...
>   =  Streaming SQL Engine for Apache Kafka® =
>   ===========================================

> Copyright 2017-2019 Confluent Inc.
> CLI v5.4.0, Server v5.4.0 located at http://localhost:8088

> ksql>
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Configuration

**Localement dans le shell**

```bash
ksql> SET 'auto.offset.reset'='earliest';
```

<!-- .element: class="big-code" -->

<br>

**Dans un fichier de configuration**

```bash
$ ksql --properties-file /etc/kafka/ksql-cli.properties

# cat ksql-cli.properties
auto.offset.reset=earliest
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Stream ksqlDB

```sql
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
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Table ksqlDB

```sql
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
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Requêtes

**Requête pull**

```sql
SELECT *
FROM CUSTOMERS
WHERE id = 1;
```

<!-- .element: class="big-code" -->

<br>

**Requête push**

```sql
SELECT *
FROM CUSTOMERS
WHERE id = 1
EMIT CHANGES;
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Types supportés

* `INTEGER`
* `BIGINT`
* `DOUBLE`
* `VARCHAR`
* `BOOLEAN`
* `STRUCT`
* `ARRAY`
* `MAP`

##==##
<!-- .slide: class="with-code" -->

# Commandes ksqlDB

* Lister les topics disponibles:

```bash
$ SHOW TOPICS;

> Kafka Topic             | Partitions | Partition Replicas
> -----------------------------------------------------------
> _schemas                | 1          | 1
> card_payments           | 3          | 1
> customers               | 3          | 1
> orders                  | 3          | 1
> -----------------------------------------------------------
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Commandes ksqlDB (suite)

* Lister les streams créées:

```bash
$ SHOW STREAMS;

> Stream Name   | Kafka Topic   | Format
> ----------------------------------------
> CARD_PAYMENTS | card_payments | JSON
> ORDERS        | orders        | JSON
> ----------------------------------------
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Commandes ksqlDB (suite)

* Lister les tables créées:

```bash
$ SHOW TABLES;

> Table Name | Kafka Topic | Format | Windowed
> ----------------------------------------------
> CUSTOMERS  | customers   | JSON   | false
> ----------------------------------------------
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Exemple de requête ksqlDB

```sql
CREATE TABLE SUSPICIOUS_ORDERS AS
  SELECT o.id AS ORDER_ID, COUNT(*) AS PAYMENT_COUNT
  FROM ORDERS o
  INNER JOIN CARD_PAYMENTS p WITHIN 5 MINUTES
  ON p.id = o.id
  GROUP BY o.id
  HAVING COUNT(*) > 1
  EMIT CHANGES;
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Exemple de requête ksqlDB (suite)

```bash
$ DESCRIBE SUSPICIOUS_ORDERS;

> Name                 : SUSPICIOUS_ORDERS
>  Field         | Type
> -------------------------------------------
>  ROWTIME       | BIGINT           (system)
>  ROWKEY        | VARCHAR(STRING)  (system)
>  ORDER_ID      | VARCHAR(STRING)
>  PAYMENT_COUNT | BIGINT
> -------------------------------------------
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Exemple de requête ksqlDB (suite)

```sql
$ SELECT ORDER_ID, PAYMENT_COUNT FROM SUSPICIOUS_ORDERS EMIT CHANGES;

> +---------------------------+---------------------------+
> |ORDER_ID                   |PAYMENT_COUNT              |
> +---------------------------+---------------------------+
> |7f21d253-c537-4a54-...     |3                          |
> |e8a9c0b5-5e1b-4ce6-...     |2                          |
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Plan d'exécution

```bash
$ EXPLAIN CTAS_SUSPICIOUS_ORDERS_2;

> ...
>  Processor: KSTREAM-MAPVALUES-0000000027 (stores: [])
>    --> KSTREAM-SINK-0000000028
>    <-- KTABLE-TOSTREAM-0000000026
>  Sink: KSTREAM-SINK-0000000028 (topic: SUSPICIOUS_ORDERS)
>    <-- KSTREAM-MAPVALUES-0000000027
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# ksqlDB - ksqlDB vs Kafka Streams

## Exercice

**step-08 : utiliser la syntaxe sql**

* Reproduire le step-07 en KSQL
  * Réaliser une jointure
  * Réaliser une aggrégation
  * Essayer de réprésenter des structures complexes
* Utiliser le format JSON !

##==##
<!-- .slide: class="with-code" -->

# API REST

* Exécuter un ensemble d'instructions ksqlDB:

```bash
$ http POST http://localhost:8088/ksql body='{"ksql": "..."}'
```

<!-- .element: class="big-code" -->

* Exécuter une requête ksqlDB:

```bash
$ http POST http://localhost:8088/query body='{"ksql": "SELECT * FROM CUSTOMERS EMIT CHANGES;"}'
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide -->

# Mode headless

* Désactivation des requêtes dynamiques
* Sécurisation des serveurs ksqlDB en production
* Versioning des requêtes à exécuter dans un fichier `sql`

##==##
<!-- .slide: class="with-code" -->

# Mode headless (suite)

**En ligne de commande**

```bash
$ ksql-server-start /etc/ksql/ksql-server.properties --queries-file /path/to/queries.sql
```

<!-- .element: class="big-code" -->

<br>

**Par fichier de configuration**

```bash
$ ksql-server-start /etc/ksql/ksql-server.properties

# cat /etc/ksql/ksql-server.properties
...
ksql.queries.file=/path/to/queries.sql
```

<!-- .element: class="big-code" -->
