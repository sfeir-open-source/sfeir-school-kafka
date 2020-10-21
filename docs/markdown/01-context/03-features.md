<!-- .slide: -->

# Fonctionnement

![h-800 center](./assets/images/kafka-architecture.svg)

##==##
<!-- .slide: -->

# Fonctionnement

**Pub/sub**

* Modèle Publish Subscribe
* Producteurs envoient dans Kafka
* Consommateurs viennent chercher dans Kafka

<p><br></p>

**Avantages**

* Découplage des producteurs et des consommateurs
* Gestion de la back pressure via une boucle de polling
* Rejeu automatique côté producteur
* Possibilité de mettre en pause la consommation

##==##
<!-- .slide: -->

# Différences avec JMS

* Très haute disponibilité
* Très gros débit même sur un cluster à 1 noeud
* Peut conserver des To voire des Po sur disque
* Temps réel ou mode batch possibles
