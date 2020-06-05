<!-- .slide: -->

# Administration

Les opérations d'administration sur un cluster sont réalisées via le CLI:

* création des topics
* détails des groupes de consommation pour un topic
* ajout des utilisateurs et des ACLs
* ...

##==##
<!-- .slide: class="with-code" -->

# Création de topics

```bash
kafka-topics \
--zookeeper localhost:2181 \
--create \
--topic customers \
--partitions 3 \
--replication-factor 1 \
--if-not-exists
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Description de topics

```bash
kafka-topics \
--zookeeper localhost:2181 \
--describe \
--topic customers

> Topic:customers   PartitionCount:3   ReplicationFactor:1   Configs
>   Topic: customers Partition 0   Leader: 1   Replicas: 1   Isr: 1
>   Topic: customers Partition 1   Leader: 1   Replicas: 1   Isr: 1
>   Topic: customers Partition 2   Leader: 1   Replicas: 1   Isr: 1
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Suppression de topics

* Suppression pure et dure:

```bash
kafka-topics --zookeeper localhost:2181 \
--delete \
--topic customers
```

<!-- .element: class="big-code" -->

* Suppression des messages en conservant le topic:

```bash
kafka-configs --zookeeper localhost:2181 \
--alter \
--entity-type topics \
--entity-name customers \
--add-config retention.ms=1000
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Production de messages

```bash
kafka-console-producer \
--broker-list localhost:9092 \
--topic customers

> {"id":1, "email":"john.doe@gmail.com", "gender":"MALE"}
> ^C
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consommation de topics

```bash
kafka-console-consumer \
--bootstrap-server localhost:9092 \
--topic customers \
--from-beginning

> {"id":1, "email":"john.doe@gmail.com", "gender":"MALE"}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Reset d'offset (début du topic)

```bash
kafka-consumer-groups \
--bootstrap-server localhost:9092 \
--reset-offsets \
--topic customers \
--group my-consumer-group \
--execute \
--to-earliest
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Reset d'offset (datetime)

```bash
kafka-consumer-groups \
--bootstrap-server localhost:9092 \
--reset-offsets \
--topic customers \
--group my-consumer-group \
--execute \
--to-datetime 2020-01-01T00:00:00.000
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# Concepts clés - CLI

## Exercice

**step-01 : utilisation du cli**

* Créer un topic Kafka:
  * 3 partitions
  * 2 copies
  * 5 minutes de rétention
  * compacté
* Produire un message
* Consommer un message
