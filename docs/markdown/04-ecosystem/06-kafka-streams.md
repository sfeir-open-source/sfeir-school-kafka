<!-- .slide: class="transition" -->

# Kafka Streams

##==##

<!-- .slide: -->

# Event Streaming

![h-800 center](./assets/images/event-streaming.svg)

##==##
<!-- .slide: -->

# Kafka Streams

* Librairie de stream processing
* Disponible depuis la version 0.10.0
* Basé sur les mêmes mécanismes que le client Kafka

<p><br></p>

**Avantages**

* Ne nécessite pas de réapprendre une nouvelle technologie
* Ne nécessite pas une architecture supplémentaire

##==##
<!-- .slide: -->

# Architecture Kafka Streams

![h-800 center](./assets/images/stream-architecture.svg)

##==##
<!-- .slide: -->

# Stream vs Table

* Une `Table` réprésente l'état courant d'une entité (un peu comme une table SQL)
* Une `Stream` représente l'historique des évènements ayant conduit à l'état courant
* Une `Stream` peut être convertie en `Table` et inversement

![h-600 center](./assets/images/stream-table.svg)

##==##
<!-- .slide: class="with-code" -->

# Client Kafka Streams

Installation avec Maven:

```xml
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-streams</artifactId>
  <version>2.5.0</version>
</dependency>
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Configuration

**Identifiant de l'application**

* `application.id`

Définit l'identifiant pour une application Kafka Streams et doit être unique au sein du cluster.

Cette valeur sera utilisée comme:

* `client.id`
* `group.id`
* préfixe pour les topics associés à l'application

##==##
<!-- .slide: class="with-code" -->

# Configuration (suite)

**Default Serdes**

* `default.key.serde`
* `default.value.serde`

Permettent de spécifier un couple sérializer/déserializer par défaut pour les clés et les valeurs.

```java
Map<String, String> serdeConfig = singletonMap("schema.registry.url", "http://localhost:8081");

SpecificAvroSerde<CardPayment> cardPaymentSerde = new SpecificAvroSerde<>();
cardPaymentSerde.configure(serdeConfig, false);

...
props.put("default.value.serde", cardPaymentSerde);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Première application Kafka Streams

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("application.id", "my-stream-app");

StreamsBuilder builder = new StreamsBuilder();
// print customers topic messages to console
builder.stream("customers").print(Printed.toSysOut().withLabel("customers"));

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Opérations stateless

* `filter`

```java
stream.filter((key, value) -> Gender.MALE == value.getGender());
```

<!-- .element: class="big-code" -->

* `branch`

```java
KStream<String, Customer>[] branches = stream.branch(
  (key, value) -> Gender.MALE == value.getGender(),
  (key, value) -> Gender.MALE != value.getGender()
);
```

<!-- .element: class="big-code" -->

* `peek`

```java
stream.peek((key, value) -> System.out.println(value));
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Opérations stateless (suite)

* `mapValues`

```java
stream.mapValues(value -> value.getEmail());
```

<!-- .element: class="big-code" -->

* `flatMapValues`

```java
stream.flatMapValues(value -> Arrays.asList(value.getEmail().split("@")));
```

<!-- .element: class="big-code" -->

* `groupBy`

```java
stream.groupBy((key, value) -> value.getGender());
```

<!-- .element: class="big-code" -->

* `groupByKey`

```java
KGroupedStream<String, Customers> groupedStream = stream.groupByKey();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Opérations stateful

* `count`

```java
stream.groupBy((key, value) -> value.getGender()).count();
```

<!-- .element: class="big-code" -->

* `reduce`

```java
stream.groupByKey().reduce((aggValue, newValue) -> aggValue + newValue);
```

<!-- .element: class="big-code" -->

* `aggregate`

```java
stream.groupByKey().aggregate(() -> 0, (key, newValue, aggValue) -> aggValue + newValue);
```

<!-- .element: class="big-code" -->

* `join`

```java
stream.join(kTable, (left, right) -> left + ":" + right);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Tumbling Window

![h-800 center](./assets/images/tumbling-window.svg)

##==##
<!-- .slide: -->

# Hopping Window

![h-800 center](./assets/images/hopping-window.svg)

##==##
<!-- .slide: -->

# Session Window

![h-800 center](./assets/images/session-window.svg)

##==##
<!-- .slide: -->

# Jointures

* Permet de joindre une `Stream` avec une `KTable`
* Permet de joindre une `Stream` avec une `Stream` en définissant une `Window`
* Basées sur les clés des messages des topics à joindre
* Nécessite que les topics aient le même nombre de partitions

##==##
<!-- .slide: -->

# Jointure Stream-KTable

![h-800 center](./assets/images/stream-table-join.svg)

##==##
<!-- .slide: -->

# Jointure Stream-Stream

![h-800 center](./assets/images/stream-stream-join.svg)

##==##
<!-- .slide: class="with-code" -->

# Application avancée Kafka Streams

```java
KStream<String, Order> orderStream = builder.stream("orders");
KStream<String, Payment> paymentStream = builder.stream("card_payments");

KStream<String, PaidOrder> paidOrderStream = orderStream.join(
  paymentStream, PaidOrder::new, JoinWindows.of(Duration.ofMinutes(5L))
);

paidOrderStream.groupByKey()
  .count()
  .filter((key, value) -> value > 1)
  .toStream()
  .to("fraudulent_orders");
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="exercice" -->

# Kafka Streams - Avro et Jointures

## Exercice

**step-07 : manipuler la librairie**

* Configurer une application Kafka Streams
  * Configurer les Serdes
  * Effectuer une jointure de streams
  * Réaliser une opération d'aggrégation
* Utiliser Avro pour faciliter la lecture du code

##==##
<!-- .slide: -->

# Interactive queries

* Accès read-only au `state store`
* Permet d'exposer des données Kafka sans créer un nouveau consommateur
* Evite la duplication de données vers une BDD

##==##
<!-- .slide: class="with-code" -->

# Exemple d'interactive queries

```java
GlobalKTable<String, String> globalKTable = builder.globalTable("customers");

ReadOnlyKeyValueStore<String, String> store = streams.store(
  globalKTable.queryableStoreName(),
  QueryableStoreTypes.keyValueStore()
);

final String customer = store.get("1");
System.out.println(customer);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Interactive queries distribuées

* Publication d'un `state store` via un endpoint RPC
* Accès au `state store` de façon distribuée
* Ouvre la possibilité à une architecture micro-services sans overhead

##==##
<!-- .slide: class="with-code" -->

# Exemple d'interactive queries distribuées

```java
Properties props = new Properties();
props.put("application.server", "localhost:1234");
...

KafkaStreams streams = new KafkaStreams(builder, props);
streams.start();

// HTTP server to respond to RPC calls
HttpServer rpcServer = HttpServer.create(new InetSocketAddress("localhost", 1234), 0);
rpcServer.createContext("/state", new MyHttpHandler());
rpcServer.start();
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Exemple d'interactive queries distribuées (suite)

```java
Collection<StreamsMetadata> storeMetadata = streams.allMetadataForStore("customers");
StreamsMetadata metadata = streams.metadataForKey(
  "customers",
  "1",
  CustomerSerdes().serializer()
);

Customer customer = http.get(
  String.format("http://%s:%d/customers/1", metadata.host(), metadata.port()),
  Customer.class
);
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests unitaires

Installation avec Maven:

```xml
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-streams-test-utils</artifactId>
  <version>2.5.0</version>
  <scope>test</scope>
</dependency>
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests unitaires (suite)

```java
@BeforeEach void setUp() {
  Properties props = new Properties();
  props.put("application.id", "my-app");
  props.put("bootstrap.servers", "localhost:9092");

  StreamsConfig streamsConfig = new StreamsConfig(props);
  Topology topology = createMyTopology();

  testDriver = new ProcessorTopologyTestDriver(streamsConfig, topology);
  store = testDriver.getKeyValueStore("my-store");
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests d'intégration

Installation avec Maven:

```xml
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-streams</artifactId>
  <version>2.5.0</version>
  <classifier>test</classifier>
  <scope>test</scope>
</dependency>
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests d'intégration (suite)

```java
static EmbeddedKafkaCluster embeddedKafka = new EmbeddedKafkaCluster(1);

@BeforeAll static void setUpAll() {
  embeddedKafka.createTopic("customers");
}

@BeforeEach void setUp() {
  Properties props = new Properties();
  props.put("bootstrap.servers", embeddedKafka.bootstrapServers());
  ...
}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests d'intégration (alternative)

`Testcontainers` est une alternative viable basée sur Docker:

```xml
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>kafka</artifactId>
  <version>1.14.0</version>
  <scope>test</scope>
</dependency>
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Tests d'intégration (alternative)

```java
@ClassRule static KafkaContainer kafkaContainer = new KafkaContainer(
  "confluentinc/cp-kafka:5.5.0"
);

@BeforeEach void setUp() {
  Properties props = new Properties();
  props.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
  ...
}
```

<!-- .element: class="big-code" -->
