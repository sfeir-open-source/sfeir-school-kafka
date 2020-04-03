<!-- .slide: class="with-code" -->

# Monitoring

Kafka utilise JMX pour exposer l'ensemble de ses métriques. Des solutions existent pour faciliter cette exposition:

**Jolokia**

Jolokia est une librairie permettant de communiquer avec un serveur JMX en utilisant des endpoints REST. Jolokia est intégré automatiquement avec [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-jmx).

**Prometheus**

Prometheus propose un exporter JMX qui peut être intégré comme agent en modifiant les options JVM au démarrage de l'application:

```bash
java -javaagent:./jmx_prometheus_javaagent-0.12.0.jar=8080:config.yaml -jar my-app.jar
```

<!-- .element: class="big-code" -->

Les métriques JMX sont alors accessibles à [http://localhost:8080/metrics](http://localhost:8080/metrics).

##==##
<!-- .slide: -->

# Métriques producteur

| Type | MBean JMX |
| ---- | --------- |
| Overall Producer | kafka.producer:type=producer-metrics,client-id=_CLIENTID_ |
| Per-Broker | kafka.producer:type=-node-metrics,client-id=_CLIENTID_,node-id=node-_BROKERID_ |
| Per-Topic | kafka.producer:type=producer-topic-metrics,client-id=_CLIENTID_,topic=_TOPICNAME_ |

##==##
<!-- .slide: -->

# Overall producer

`record-error-rate`

* Indique le nombre de messages qui n'ont pas pu être envoyés (épuisement du nombre de rejeux)
* Peut-être la métrique la plus importante à remonter pour un producteur

`request-latency-avg`

* Indique le temps moyen que prend une requête pour être traitée
* Doit être plus ou moins constant au fil du temps

`record-send-rate`

* Indique le nombre de messages produits par seconde.

##==##
<!-- .slide: -->

# Overall producer (suite)

`request-size-avg`

* Indique la taille moyenne en octets des batchs envoyés à Kafka
* Peut être combinée avec la métrique `record-size-avg` qui donne la taille moyenne des messages en octets

`record-queue-time-avg`

* Indique le temps moyen d'attente d'un message dans la queue du producteur avant qu'il ne soit envoyé à Kafka
* Influencé par la configuration du producteur, notamment le `batch.size` et le `linger.ms`

##==##
<!-- .slide: -->

# Métriques consommateur

| Type | MBean JMX |
| ---- | --------- |
| Overall Consumer | kafka.consumer:type=consumer-metrics,client-id=_CLIENTID_ |
| Fetch Manager | kafka.consumer:type=consumer-fetch-manager,client-id=_CLIENTID_ |
| Per-Topic | kafka.consumer:type=consumer-fetch-manager,client-id=_CLIENTID_,topic=_TOPICNAME_ |
| Per-Broker | kafka.consumer:type=consumer-node-metrics,client-id=_CLIENTID_,node-id=node-_BROKERID_ |
| Coordinator | kafka.consumer:type=consumer-coordinator-metrics,client-id=_CLIENTID_ |

##==##
<!-- .slide: -->

# Fetch Manager

`fetch-latency-avg`

* Indique le temps moyen des requêtes `fetch` au cluster Kafka.

`records-consumed-rate`

* Indique le nombre de messages consommés par seconde.
* Peut être combiné avec la métrique `bytes-consumed-rate` pour estimer le traffic côté consommateur.

`fetch-rate`

* Indique le nombre de requêtes `fetch` émises par seconde par le consommateur.
* Peut être combiné avec la métrique `fetch-size-avg` qui indique la taille moyenne des requêtes `fetch` en octets.

##==##
<!-- .slide: -->

# Consumer coordinator

`sync-time-avg`

* Indique le temps moyen pris par les opérations de coordination du groupe de consommation en millisecondes.
* Peut être combiné avec la métrique `sync-rate` indiquant le nombre de ces opérations par seconde.

`commit-latency-avg`

* Indique le temps moyen requis pour committer un offset en secondes.

`assigned-partitions`

* Indique le nombre de partitions assignées à une instance de consommation.
* Permet de détecter une répartition inégale des partitions au sein du groupe de consommation.

##==##
<!-- .slide: class="with-code" -->

# Lag

* Métrique la plus critique pour tout consommateur Kafka
* Calculé par partition et groupe de consommation selon la formule suivante:

```
Pour une partition k,
Lag(k) = Offset du dernier message produit - Dernier offset committé
```

<!-- .element: class="big-code" -->

Cette métrique est à récolter en interrogeant le cluster à intervalles réguliers.

Les clients ne doivent pas calculer eux-mêmes leur lag! Utiliser un process externe et remonter les métriques vers le système de monitoring.

Zalando est un utilisateur intensif de Kafka et propose une application Scala pour calculer le lag des consommateurs:

* [zalando-incubator/remora](https://github.com/zalando-incubator/remora)
