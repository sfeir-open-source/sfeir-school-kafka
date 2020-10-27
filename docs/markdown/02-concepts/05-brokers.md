<!-- .slide: -->

# Brokers

* Reçoivent les requêtes des producteurs et des consommateurs
* Gèrent l'élection des partitions leader et la réplication
* Permettent la scalabilité horizontale et la résilience

##==##
<!-- .slide: class="with-code" -->

# Controller

* Broker responsable de l'élection des partitions leader
* Unique au sein du cluster ou splitbrain
* Premier broker à créer le znode `/controller` dans Zookeeper:

```bash
$ zookeeper-shell localhost:2181
> get /controller
> {"version":1,"brokerid":1,"timestamp":"1603835009852"}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Réplication

* Leader replica : gèrent les requêtes d'écriture ET de lecture
* Follower replica : répliquent les messages des partitions leader

![h-600 center](./assets/images/partition-leader.svg)

##==##
<!-- .slide: -->

# Réplication (suite)

* Kafka maintient à jour la liste des replicas à jour (**I**n **S**ync **R**eplicas)
* Un replica est à jour s'il parvient à répliquer les messages à temps
* Controllé en calculant le **LAG** des replicas
