<!-- .slide: -->

# Librairies

Des clients existent dans la plupart des langages:

* Python
* Go
* .Net
* C++

Le client officiel Java est le plus stable et suit les évolutions du protocole Kafka.

La plupart des clients se basent sur [librdkafka](https://github.com/edenhill/librdkafka), une implémentation du protocole en C++.

<br>

_Nous utiliserons le client officiel Java pour le reste de la formation._

##==##
<!-- .slide: class="with-code" -->

# Client Java

Installation avec Maven:

```xml
<!-- library for both producer and consumer features -->
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-clients</artifactId>
  <version>2.4.0</version>
</dependency>
```

<!-- .element: class="big-code" -->

<br>

Un client peut communiquer avec un broker plus récent ou plus vieux (depuis la 0.10.2).
