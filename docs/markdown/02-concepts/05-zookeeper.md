<!-- .slide: -->

# Zookeeper

* Projet Apache open-source
* Fournit des mécanismes de synchronisation
* Permet le stockage de données éphémères et/ou persistées
* Peut être organisé en cluster (`ensemble`)
* Utilisé par de nombreux projets open-source:
  * Kafka
  * Hadoop
  * Nifi

##==##
<!-- .slide: class="with-code" -->

# Zookeeper (suite)

Kafka utilise Zookeper pour:

* Gérer les groupes de consommation
* Détecter les pertes de brokers
* Stocker les metadata et les ACLs

Stockage sous forme d'arborescence de nœuds (`znode`):

```bash
$ zookeeper-shell localhost:2181
> ls /
> [admin, brokers, cluster, config, consumers, controller, ...]
> get /config/topics/customers
> {"version":1,"config":{"retention.ms":"604800000"}}
```

<!-- .element: class="big-code" -->
