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
    System.out.println(metadata.offset());
  }
});
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Configuration

| Nom | Description |
| --- | ----------- |
| **bootstrap.servers** | Liste de brokers Kafka (au moins un) |
| **key.serializer** | Serializer pour la clé du message |
| **value.serializer** | Serializer pour la valeur du message |
| **acks** | Nombre minimum de replicas à jour |
| **retries** | Nombre de rejeux en cas d'erreur transiente |
| **max.in.flight.requests.per.connection** | Nombre maximum de rejeux en parallèle |
| **compression.type** | Algorithme de compression |
| **linger.ms** | Temps maximum d'attente avant l'envoi d'un batch |
| **batch.size** | Taille maximale d'un batch avant envoi |
| **client.id** | Identifiant du producteur |

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
