<!-- .slide: -->

# Producteurs

* Produisent les messages vers les topics
* Convertissent les messages en `byte[]`
* Batching et compression des messages destinés à une partition
* Peuvent choisir de router un message vers une partition précise

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

* Garantir une distribution équitable des messages dans chaque partition
* Partitionner sémantiquement en associant une partition à une population de messages

Les messages avec la même clé arriveront toujours dans la même partition tant que le nombre de partitions du topic reste inchangé.

Lorsque l'on utilise une clé pour nos messages, il faut donc être sûr d'avoir une cardinalité suffisante pour permettre une bonne distribution des données.

##==##
<!-- .slide: -->

# Consommateurs

* Consomment les messages d'un ou plusieurs topics par batchs
* Peuvent s'assigner toute ou partie des partitions d'un topic
* Convertissent les messages au format binaire en POJO
* Peuvent être regroupés en `Consumer Group` pour distribuer la consommation d'un topic

##==##
<!-- .slide: -->

# Groupe de consommation

* Par défaut un consommateur récupère l'intégralité des messages d'un topic.
* Pour distribuer la consommation, il est nécessaire d'associer plusieurs consommateurs à un même `Consumer Group`

<br>

![h-500 center](./assets/images/range-assigner.svg)

##==##
<!-- .slide: -->

# Groupe de consommation (suite)

* Un consommateur `commit` l'offset des messages qu'il traite
* Permet aux consommateurs de reprendre là où ils s'étaient arrêtés
* Par défaut le commit est effectué automatiquement à intervalle régulier
* Le commit peut être effectué manuellement pour plus de contrôle

<br>

![h-400 center](./assets/images/consumer-offset.svg)

##==##
<!-- .slide: -->

# Groupe de consommation (suite)

Kafka assigne automatiquement les partitions pour chaque consommateur d'un même groupe de consommation.

Deux stratégies sont disponibles:

* `range`
* `round-robin`

En règle générale, il faut favoriser la stratégie `round-robin` qui permet une assignation équilibrée des partitions.

S'il y a plus de consommateurs que de partitions alors certains consommateurs ne feront rien.

Si un consommateur tombe en erreur alors Kafka ré assigne l'ensemble des partitions aux consommateurs présents. Cette opération est appelée un `rebalance`.
