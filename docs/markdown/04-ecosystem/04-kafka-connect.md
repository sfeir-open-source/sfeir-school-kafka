<!-- .slide: -->

# Kafka Connect

* Framework développé par Confluent pour la version 0.9 de Kafka

* Permet l'extraction et l'intégration de données depuis ou vers des systèmes externes

![h-600 center](./assets/images/kafka-connect.svg)

##==##
<!-- .slide: -->

# Kafka Connect (suite)

* **Scalabilité**
  * Exploite les mêmes mécanismes que les producteurs et consommateurs Kafka
* **Simplicité**
  * Expose une API REST permettant de démarrer simplement de nouveaux traitements
* **Schéma**
  * S'interface avec le Schema Registry étudié précédemment

##==##
<!-- .slide: -->

# Connecteurs

* Les connecteurs sont des jobs en charge de transférer des données entre Kafka et un système tiers
* Consistent simplement en des producteurs et des consommateurs basiques
* Il existe deux types de connecteurs:
  * Les connecteurs de type `Source` consomment depuis un système externe et écrivent dans un ou des topic(s) Kafka
  * Les connecteurs de type `Sink` consomment des messages Kafka pour les écrire dans un système externe

##==##
<!-- .slide: -->

# Connecteurs (suite)

Confluent embarque de nombreux types de connecteurs:

* FileSystem
* S3
* JDBC
* Elasticsearch
* Kinesis
* HDFS
* ...

<br>

Une liste exhaustive de connecteurs supportés peut être trouvée sur le [Hub Confluent](https://www.confluent.io/hub/).

##==##
<!-- .slide: -->

# Connecteur JDBC

![h-800 center](./assets/images/kafka-connect-jdbc.svg)

##==##
<!-- .slide: -->

# Converters

Kafka Connect dispose de `converters` intégrés à appliquer sur la clé ou la valeur:

* `AvroConverter`
* `JSONConverter`
* `StringConverter`
* `ByteArrayConverter`

##==##
<!-- .slide: class="with-code" -->

# Converter Avro

Il est recommendé d'utiliser un converter Avro:

```properties
key.converter=io.confluent.connect.avro.AvroConverter
key.converter.schema.registry.url=http://localhost:8081
value.converter=io.confluent.connect.avro.AvroConverter
value.converter.schema.registry.url=http://localhost:8081
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide -->

# Transformers

Kafka Connect propose également d'intégrer des transformations légères sur chaque message en transit.

Quelques transformations possibles:

* `InsertField`: ajout d'un champ avec une valeur statique
* `ReplaceField`: filtre ou renomme un champ
* `MaskField`: remplace un champ avec une valeur "nulle"
* `RegexRouter`: modifie le topic de destination d'un message
* ...

<br>

L'ensemble des transformations disponibles est accessible sur la [documentation Kafka](http://kafka.apache.org/documentation.html#connect_transforms).

##==##
<!-- .slide: class="with-code" -->

# Modes de déploiement

Kafka Connect peut être déployé de deux façons:

* standalone
* distributed

Le mode `distributed` permet de distribuer les jobs sur plusieurs instances Kafka Connect. En règle générale, Kafka Connect est toujours déployé en mode `distributed`:

```bash
$ connect-distributed connect-distributed.properties
```

<!-- .element class="big-code" -->

La propriété `group.id` permet de constituer un cluster d'instances Kafka Connect:

```properties
# connect-distributed.properties
group.id=my-connect-cluster
...
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# API REST

* Lister les connecteurs actifs:

```bash
$ http GET http://localhost:8083/connectors
```

<!-- .element class="big-code" -->

* Créer un nouveau connecteur:

```bash
$ http POST http://localhost:8083/connectors
```

<!-- .element class="big-code" -->