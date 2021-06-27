<!-- .slide: class="with-code" -->

# Premier consommateur

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.deserializer", StringDeserializer.class);
props.put("value.deserializer", StringDeserializer.class);
props.put("group.id", "my-consumer-group");

Consumer<String, String> consumer = new KafkaConsumer<>(props);
...
consumer.close();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* Consommer un seul topic:

```java
consumer.subscribe(Collections.singletonList("customers"));
```

<!-- .element: class="big-code" -->

* Consommer une liste de topics:

```java
consumer.subscribe(Arrays.asList("customers_fr", "customers_uk"));
```

<!-- .element: class="big-code" -->

* Consommer les topics pour une regex:

```java
consumer.subscribe(Pattern.compile("customers_.*"));
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* Polling de messages

```java
while (true) {
  ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
  records.forEach(record -> {
    Object key = record.key();
    Object value = record.value();
    ...
  });
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* Commit manuel des offsets

```java
records.forEach(record -> {
  TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
  OffsetAndMetadata metadata = new OffsetAndMetadata(record.offset() + 1, "");
  // suivi des derniers offsets traités
  offsets.put(topicPartition, metadata);
});

// commit asynchrone des offsets
consumer.commitAsync(offsets, null);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* Gestion du rebalance

```java
consumer.subscribe(Collections.singletonList("customers"), new CustomerRebalanceListener());

public class CustomerRebalanceListener implements ConsumerRebalanceListener {
  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {}

  public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
    // commit offsets before partitions are revoked!
    consumer.commitSync(offsets);
  }
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* Arrêt du consommateur

```java
try {
  ...
} catch (final WakeupException e) {
} catch (final Exception e) {
  log.error("Something bad happened", e);
} finally {
  try {
    consumer.commitSync(offsets);
  } finally {
    consumer.close();
  }
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier consommateur (suite)

* La méthode `close` ne peut pas être appelée dans un thread différent de la boucle de polling
* La méthode `wakeup` peut être appelée de n'importe quel thread
* Un consommateur Kafka ne peut pas être partagé contrairement à un producteur!

```java
// fermeture du consommateur à l'arrêt de la jvm
Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
```

<!-- .element: class="big-code" -->

</br>

L'arrêt correct d'un consommateur déclenchera immédiatement un `rebalance`.

##==##
<!-- .slide: class="with-code" -->

# Changement d'offset

* Possible de mettre à jour dynamiquement les offsets d'un consommateur:

```java
consumer.subscribe(Collections.singletonList("customers"));
...
// positionnement au début du topic
consumer.seekToBeginning(consumer.assignment());
// positionnement à la fin du topic
consumer.seekToEnd(consumer.assignment());
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Changement d'offset (suite)

* La recherche d'offset peut être effectuée par `timestamp`:

```java
Map<TopicPartition, Long> timestamps = ...
Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(timestamps);

for (Entry<TopicPartition, OffsetAndTimestamp> offset : offsets.entrySet()) {
  consumer.seek(offset.getKey(), offset.getValue().offset());
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Configuration

| Nom | Description |
| --- | ----------- |
| **bootstrap.servers** | Liste de brokers Kafka (au moins un) |
| **key.deserializer** | Deserializer pour la clé du message |
| **value.deserializer** | Deserializer pour la valeur du message |
| **group.id** | Nom du groupe de consommation |
| **enable.auto.commit** | Active le commit automatique des offsets |
| **auto.offset.reset** | Définit la stratégie en cas de première connexion |
| **fetch.min.bytes** | Nombre minimum d'octets par batch |
| **partition.assignment.strategy** | Stratégie d'assignation des partitions au sein du groupe |
| **client.id** | Identifiant du consommateur |

##==##
<!-- .slide: class="exercice" -->

# Développement - Consommateur

## Exercice

**step-03 : consommation de messages**

* Consommer un message depuis Kafka:
  * Configurer le `KafkaConsumer`
  * S'abonner au topic `customers`
  * Afficher les messages dans la console
