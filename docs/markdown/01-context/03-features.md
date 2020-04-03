<!-- .slide: -->

# Fonctionnement

![h-800 center](./assets/images/kafka-architecture.svg)

##==##
<!-- .slide: -->

# Fonctionnement

**Pub/sub**

* Modèle Publish / Subscribe
* Producteurs produisent dans Kafka
* Consommateurs reçoivent depuis Kafka

<p><br></p>

**Avantages**

* Les producteurs ne se préocuppent pas de savoir qui consommera la donnée
* Les consommateurs consomment les données à leur rythme via une boucle de polling
* Rejeu automatique en cas d'erreur d'envoi
* Un consommateur peut arrêter de consommer et reprendre plus tard

##==##
<!-- .slide: -->

# Différences avec JMS

* Haute disponibilité
* Très gros débit même en standalone
* Peut conserver des To voire des Po sur disque
* Temps réel ou mode batch possibles
