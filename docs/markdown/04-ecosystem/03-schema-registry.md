<!-- .slide: class="transition" -->

# Schema Registry

##==##

<!-- .slide: -->

# Schema Registry

Composant développé par Confluent:

* Enregistre et versionne les schémas Avro
* Expose une API REST pour récupérer un schéma
* Vérifie la rétro compatibilité d'une nouvelle version de schéma
* Ne nécessite plus d'envoyer le schéma avec chaque message

##==##
<!-- .slide: -->

# Schema Registry (suite)

![h-800 center](./assets/images/schema-registry.svg)

##==##
<!-- .slide: -->

# Avro De/Serializer

Confluent propose un `KafkaAvroSerializer` et un `KafkaAvroDeserializer` s'intégrant nativement avec Avro et le Schema Registry:

1. Le producteur vérifie s'il possède le schéma associé au message. Si ce n'est pas le cas, le schéma est généré et pushé au Schema Registry. Finalement il est mis en cache par le producteur.

1. Le Schema Registry s'assure que si le schéma a évolué, il respecte toujours le niveau de compatibilité configuré au topic. Sinon le `KafkaAvroSerializer` échoue et stoppe le producteur.

1. Si tout se passe bien, le producteur envoie le message au format Avro précédé de l'ID du schéma dans le Schema Registry.

1. Les consommateurs récupèrent le schéma grâce à son ID et peuvent déserialiser le message en toute sécurité.

##==##
<!-- .slide: class="with-code" -->

# Avro Serializer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", StringSerializer.class);
props.put("value.serializer", KafkaAvroSerializer.class);
props.put("schema.registry.url", "http://localhost:8081");

Producer<String, Customer> producer = new KafkaProducer<>(props);
...
producer.close();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Avro Serializer (suite)

```java
Customer customer = Customer.newBuilder()
  .setId(1)
  .setEmail("john.doe@gmail.com")
  .setGender(Gender.MALE)
  .build();

producer.send("customers", "1", customer);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Avro Deserializer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.deserializer", StringDeserializer.class);
props.put("value.deserializer", KafkaAvroDeserializer.class);
props.put("schema.registry.url", "http://localhost:8081");
props.put("specific.avro.reader", "true");
props.put("group.id", "my-consumer-group");

Consumer<String, Customer> consumer = new KafkaConsumer<>(props);
...
consumer.close();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# API REST

* Récupérer un schéma avec son ID:

```bash
$ http GET http://localhost:8081/schemas/ids/(int: id)
```

<!-- .element: class="big-code" -->

* Lister tous les sujets présents:

```bash
$ http GET http://localhost:8081/subjects
```

<!-- .element: class="big-code" -->

* Enregister un nouveau schéma:

```bash
$ http POST http://localhost:8081/subjects/(string: subject)
```

<!-- .element: class="big-code" -->

* Ajouter une nouvelle version d'un schéma:

```bash
$ http POST http://localhost:8081/subjects/(string: subject)/versions
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Niveau de compatibilité

Le Schema Registry applique un contrôle de compatibilité pour tout nouveau schéma enregistré.

Ce contrôle peut être de différents types:

* `BACKWARD` _(par défaut)_: les consommateurs en version N+1 peuvent lire les messages produits avec la version N du schéma
* `FORWARD`: les consommateurs en version N peuvent lire les messages produits avec la version N+1 du schéma
* `FULL`: le schéma respecte les modes `BACKWARD` et `FORWARD`
* `NONE`: pas de contrôle de compatibilité

<br>

En cas de non-respect du niveau de compatibilité, la tentative d'ajout du schéma échouera.

##==##
<!-- .slide: class="with-code" -->

# Niveau de compatibilité (suite)

* Définir la compatibilité globalement:

```bash
$ http PUT http://localhost:8081/config body='{"compatibility":"FULL"}'
```

<!-- .element: class="big-code" -->

* Définir la compatibilité par sujet:

```bash
$ http PUT http://localhost:8081/config/(string: subject) body='{"compatibility":"FULL"}'
```

<!-- .element: class="big-code" -->

* Valider la compatibilité d'un schéma:

```bash
$ http POST http://localhost:8081/compatibility/subjects/(string: subject)/versions/(versionId: version)
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# Schema Registry - Client Kafka

## Exercice

**step-05 : produire et consommer de l'avro**

* Ré utiliser les sources Avro du `Customer`
* Configurer le `KafkaProducer`
* Configurer le `KafkaConsumer`
