<!-- .slide: class="with-code" -->

# Topics

* Messages regroupés au sein de topics
* Ajout de messages uniquement (pas d'update)
* Divisé en partitions pour assurer la scalabilité horizontale
* Aucune garantie sur l'ordre de lecture ou d'écriture
* Peut être repliqué afin de se prémunir de la perte de données
* Créé automatiquement à la production / consommation ou manuellement:

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
* L'ordre des messages est garanti au sein d'une partition (FIFO)

<br>

![h-500 center](./assets/images/topic-partition.svg)

##==##
<!-- .slide: -->

# Offsets

Indice d'un message au sein d'une partition avec les propriétés suivantes:

* Toujours positif
* Unique au sein d'une partition
* Incrémenté de façon croissante

<br>

Un message est identifié par le triplet:

* `topic`
* `partition`
* `offset`

Un timestamp est associé à chaque message, pratique pour effectuer une recherche par dichotomie.

##==##
<!-- .slide: class="with-code" -->

# Rétention

* Par défaut les messages vieux de 7 jours sont supprimés
* Par défaut un topic peut occuper tout l'espace disque nécessaire
* La rétention peut être configurée au niveau du broker ou définie par topic:
  * `retention.ms`: âge maximal **PAR PARTITION**
  * `retention.bytes`: taille maximale **PAR PARTITION**
* La rétention peut être mise à jour au runtime:

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

* Par défaut les messages sont conservés 1 semaine
* Possibilité sinon de ne conserver que la dernière version associée à une clé
* Tous les messages du topic doivent avoir une clé de renseignée
* Compation activée via le paramètre `cleanup.policy`:
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
