<!-- .slide: -->

# Théorème CAP

![h-800 center](./assets/images/cap-theorem.svg)

##==##
<!-- .slide: class="quote" -->

# Théorème CAP

<p class="quotation center">
There is no such thing as a free lunch.
</p>

##==##
<!-- .slide: class="with-code" -->

# Haute disponibilité

**Broker**

* Autoriser l'élection d'une partition comme leader même si `under-replicated`
* N'attendre qu'un seul replica pour qu'un message soit considéré reçu

```properties
unclean.leader.election = true
min.insync.replicas = 1
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consistance

**Broker**

* Empêcher l'élection d'une partition comme leader si `under-replicated`
* Attendre le retour des **TOUS** les replicas

```properties
unclean.leader.election = false
min.insync.replicas = <replication_factor>
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consistance

**Producteur**

* Attendre que **TOUS** les réplicas aient reçu le message
* Re tenter l'envoi du message de façon infinie
* Eviter les doublons de message suite à une erreur réseau

```java
properties.put(ACKS_CONFIG, "all");
properties.put(RETRIES_CONFIG, Integer.MAX_VALUE);
properties.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_CONFIG, "1");
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consistance

**Consommateur**

* Consommer depuis le début du topic à la première souscription
* Désactiver le commit automatique des offsets

```java
properties.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
properties.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Performances

**Producteur**

* Attendre que le leader ait reçu le message
* Ne pas attendre avant d'envoyer un message
* Réduire la taille des batchs à envoyer

```java
properties.put(ACKS_CONFIG, "1");
properties.put(LINGER_MS_CONFIG, "0");
properties.put(BATCH_SIZE_CONFIG, "<low_value>");
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Performances

**Consommateur**

* Réduire la taille minimale des batchs à retourner au consommateur

```java
properties.put(FETCH_MIN_BYTES_CONFIG, "1");
```

<!-- .element class="big-code" -->
