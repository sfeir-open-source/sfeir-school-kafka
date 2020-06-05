<!-- .slide: class="with-code" -->

# Premier producteur

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", StringSerializer.class);
props.put("value.serializer", StringSerializer.class);

Producer<String, String> producer = new KafkaProducer<>(props);
...
producer.close();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Premier producteur (suite)

```java
ProducerRecord<String, String> record = new ProducerRecord("customers", "1", "...");

producer.send(record, (metadata, exception) -> {
  if (exception != null) {
    // manage exception here
  } else {
    System.out.println(record.offset());
  }
});
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# Développement - Producteur

## Exercice

**step-02 : production de messages**

* Produire un message dans Kafka:
  * Configurer le `KafkaProducer`
  * Créer un `Record`
  * Envoyer le message dans le topic `customers`
* S'assurer de l'envoi
