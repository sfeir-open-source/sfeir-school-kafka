<!-- .slide: -->

# Zookeeper

* Projet opensource de la fondation Apache
* Fournit des mécanismes de synchronisation
* Permet le stockage de données éphémères et/ou persistées
* Peut être organisé en cluster (`ensemble`)
* Utilisé par de nombreux projets open-source:
  * Hadoop
  * Flink
  * Nifi

##==##
<!-- .slide: class="with-code" -->

# Zookeeper (suite)

Zookeeper est un composant essentiel de Kafka:

* Stockage de la configuration
* Election du broker controller
* Gestion des groupes de consommation

Stockage sous forme d'arborescence de nœuds (`znode`):

```bash
$ zookeeper-shell localhost:2181
> ls /
> [admin, brokers, cluster, config, consumers, controller, ...]
> get /config/topics/customers
> {"version":1,"config":{"retention.ms":"604800000"}}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Zookeeper (suite)

Depuis Kafka 2.8, Kafka peut utiliser son propre algorithme de consensus.

Zookeeper n'est donc **plus nécessaire** au fonctionnement d'un cluster Kafka.

<br>

Voir le [KIP-500](https://cwiki.apache.org/confluence/display/KAFKA/KIP-500%3A+Replace+ZooKeeper+with+a+Self-Managed+Metadata+Quorum)
pour plus de détails.