<!-- .slide: -->

# Producteurs

* Créent les messages et les envoient vers les topics
* Doivent convertir les messages en binaire
* Gérent l'envoi par batch et la compression des messages
* Décident de cibler ou non une partition précise

##==##
<!-- .slide: -->

# Partionnement

Deux stratégies pour partitionner les messages:

* `round-robin` si la clé du message est nulle
* `hash(key) % number_of_partitions`

Le producteur peut définir sa propre stratégie en implémentant l'interface `Partitioner`.

```java
public class MyCustomPartitioner implements Partitioner {

  @Override
  public int partition(
          String topic,
          Object key,
          byte[] keyBytes,
          Object value,
          byte[] valueBytes,
          Cluster cluster
  ) {
    int numberOfPartitions = cluster.partitionCountForTopic(topic);
    return Utils.murmur2(keyBytes) % numberOfPartitions;
  }

}
```

##==##
<!-- .slide: -->

# Partitionnement (suite)

Le partitionnement permet de:

* Garantir une distribution équitable des messages au sein d'un topic
* Associer une partition à une population de messages

Les messages avec la même clé seront toujours envoyés dans une même partition.

Les clés doivent être suffisamment diversifiées pour garantir une bonne distribution des messages.

##==##
<!-- .slide: -->

# Consommateurs

* Consomment les messages d'un ou de plusieurs topics
* Décident de consommer un topic ou des partitions en particulier
* Convertissent les messages vers le format adéquat
* Peuvent être regroupés en groupe de consommation ou non

##==##
<!-- .slide: -->

# Groupe de consommation

* Par défaut un consommateur consomment toutes les partitions d'un topic
* Plusieurs consommateurs peuvent former un groupe de consommation pour distribuer la charge

<br>

![h-500 center](./assets/images/range-assigner.svg)

##==##
<!-- .slide: -->

# Groupe de consommation (suite)

* Le consommateur enregistre l'offset du dernier message consommé (`commit`)
* Permet aux consommateurs de reprendre là où ils s'étaient arrêtés
* Par défaut le commit est effectué automatiquement et à intervalle régulier
* Il peut être effectué manuellement pour plus de contrôle

<br>

![h-400 center](./assets/images/consumer-offset.svg)

##==##
<!-- .slide: -->

# Groupe de consommation (suite)

Kafka assigne automatiquement les partitions aux consommateurs d'un même groupe.

Deux stratégies sont disponibles:

* `range`
* `round-robin`

En général la stratégie `round-robin` permet une assignation équilibrée des partitions.

S'il y a plus de consommateurs que de partitions, ils resteront en attente.

En cas de panne d'un consommateur, Kafka déclenche automatiquement une réassignation (`rebalance`).
