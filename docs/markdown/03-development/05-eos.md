<!-- .slide: -->

# Exactly Once Semantics

Dans un système distribué, les garanties de traitement d'un message sont les suivantes:

* `At-most once`
* `At-least once`
* `Exactly once`

L'`exactly once` implique que le rejeu d'un message n'entraîne pas de doublons dans Kafka.

![h-400 center](./assets/images/duplicate.svg)

##==##
<!-- .slide: -->

# Exactly Once Semantics (suite)

* Introduit en version 0.11
* Supporté par les clients Kafka et Kafka Streams
* Mise à jour des metadata associés aux messages

##==##
<!-- .slide: -->

# Producteur EOS

**Activer le mode transactionnel**

* `enable.idempotence=true`

Permet de rendre le producteur idempotent, un retry n'entraînera pas de duplication dans une partition.

**Créer une transaction cross-producers**

* `transactional.id`

Permet de s'assurer que les transactions partageant le même `transactional.id` aient été commitées avant d'en démarrer de nouvelles.

##==##
<!-- .slide: class="with-code" -->

# Producteur EOS (suite)

```java
producer.initTransactions();
try {
  producer.beginTransaction();
  producer.send(record1);
  producer.send(record2);
  producer.sendOffsetsToTransaction(...);
  producer.commitTransaction();
} catch (final ProducerFencedException e) {
  producer.close();
} catch (final KafkaException e) {
  producer.abortTransaction();
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Consommateur EOS

**Consommer uniquement les messages committés**

* `isolation.level=read_committed`

Indique au consommateur de ne récupérer que les messages marqués committés.

Permet d'éviter la duplication de messages.

Par défaut ce paramètre est placé à `read_uncommitted`.
