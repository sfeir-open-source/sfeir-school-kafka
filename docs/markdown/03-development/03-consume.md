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

```java
while (true) {
  ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
  records.forEach(record -> {
    System.out.println(record.value());

    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
    OffsetAndMetadata metadata = new OffsetAndMetadata(record.offset() + 1, "");
    offsets.put(topicPartition, metadata);
  });

  consumer.asyncCommit(offsets, null);
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# Développement - Consommateur

## Exercice

**step-03 : consommation de messages**

* Consommer un message depuis Kafka:
  * Configurer le `KafkaConsumer`
  * S'abonner au topic `customers`
  * Afficher les messages dans la console
