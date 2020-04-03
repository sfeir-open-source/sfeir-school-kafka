<!-- .slide: -->

# Administration

Le CLI Kafka se base sur des classes Scala présentes dans les sources de Kafka. Il est nécessaire d'être positionné sur un noeud du cluster pour utiliser la ligne de commande Kafka.

La ligne de commande Kafka permet d'effectuer bon nombre d'opérations sur le cluster:

* création de topics (nombre de partitions, facteur de réplication, ...)
* description des groupes de consommation sur un topic
* affichage des partitions rencontrant des retards de réplication
* ajout d'utilisateurs si un mécanisme de sécurité est activé
* production vers un topic depuis la console
* consommation d'un topic et affichage dans la console
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
