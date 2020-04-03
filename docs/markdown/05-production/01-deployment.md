<!-- .slide: -->

# Déploiement

Les clients Kafka sont de simples librairies à ajouter au projet:

* Pas besoin d'une architecture supplémentaire
* Pas besoin de dépendences externes

##==##
<!-- .slide: class="with-code" -->

# Intégration Spring

Spring propose une intégration simplifiée de Kafka avec le module `spring-kafka`:

**Producteur**

```java
public void sendMessage(String message) {
  kafkaTemplate.send("customers", message);  
}
```

<!-- .element: class="big-code" -->

**Consommateur**

```java
@KafkaListener(topics = "customers", groupId = "my-consumer-group")
public void onReceiveMessage(String message) {
  // do the processing
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Spring Actuator

```java
public Health health() {
  try {
    final String clusterId = adminClient.describeCluster().clusterId().get();
    return Health.up()
      .withDetail("cluster_id", clusterId)
      .build();

  } catch (final Exception e) {
    return Health.down(e).build();
  }
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide -->

# Choix du client

![h-800 center](./assets/images/clients-comparison.svg)
