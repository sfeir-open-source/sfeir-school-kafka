<!-- .slide: class="with-code" -->

# Topics

* Conteneur de messages regroupés par format ou entité métier
* Ecriture en mode `append-only` => **PAS D'UPDATE**
* Divisé en une ou plusieurs partition(s) pour assurer la scalabilité horizontale
* Pas de garantie d'ordre de lecture ou d'écriture
* Peut être repliqué pour garantir la non perte de données
* Créé automatiquement dès qu'un client produit ou consomme ou via le CLI:

```bash
kafka-topics \
--zookeeper localhost:2181 \
--create \
--topic customers \
--partitions 3 \
--replication-factor 1
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Partitions

* Structure de données élémentaire d'un topic
* Détermine le débit maximum pour la lecture et l'écriture dans un topic
* Garantie d'ordre des messages au sein d'une partition => FIFO

<br>

![h-500 center](./assets/images/topic-partition.svg)

##==##
<!-- .slide: -->

# Offsets

L'offset est l'indice d'un message au sein d'une partition et possède les propriétés suivantes:

* Toujours positif
* Unique au sein d'une partition
* Incrémenté de façon croissante

<br>

Un message peut être identifié par le triplet:

* `topic`
* `partition`
* `offset`

Le `timestamp` présent dans les metadata peut également être utilisé pour effectuer une recherche par dichotomie.

##==##
<!-- .slide: class="with-code" -->

# Rétention

* Par défaut un topic ne conserve que les messages de moins de sept jours
* Par défaut il n'y a pas de limite à l'occupation disque d'un topic
* La rétention peut être configurée au niveau du broker et surchargée par topic:
  * `retention.ms`: âge maximal **PAR PARTITION**
  * `retention.bytes`: taille maximale **PAR PARTITION**
* La mise à jour de la rétention peut se faire en runtime après la création du topic:

```bash
kafka-topics \
--zookeeper localhost:2181 \
--alter \
--topic customers \
--config retention.ms=604800000
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Compaction

* Par défaut les messages sont supprimés passée la période de rétention
* Possible à la place de ne conserver que la dernière valeur d'un message par rapport à sa clé
* Nécessite que tous les messages du topic aient une clé ou la compaction échouera
* Contrôlée par le paramètre `cleanup.policy`:
  * `delete` (par défaut)
  * `compact`

```bash
kafka-topics \
--zookeeper localhost:2181 \
--alter \
--topic customers \
--config cleanup.policy=compact
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Compaction (suite)

![h-800 center](./assets/images/compaction.svg)
