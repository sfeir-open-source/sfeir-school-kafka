<!-- .slide: -->

# Securité

Kafka peut exposer plusieurs ports:

* `PLAINTEXT`: pas d'authentification et transfert en clair
* `SSL`: authentification et chiffrement
* `SASL`: authentification
* `SSL + SASL`: chiffrement SSL et authentification SASL

Le client Kafka décide du port à contacter.

##==##
<!-- .slide: -->

# SASL

_Simple Authentication and Security Layer_

SASL supporte différents mécanismes pour véhiculer les informations d'authentification:

* `PLAIN`: le username et password sont envoyés en clair
* `SCRAM-SHA-256`, `SCRAM-SHA-512`: les mots de passe sont "hashés" et "salés"
* `GSSAPI`: SSO avec Kerberos

##==##
<!-- .slide: -->

# Zero Copy

* Transfert immédiat de socket à socket
* Pas d'overhead induit par la JVM
* **Impossible si le SSL est activé**

![h-600 center](./assets/images/zero-copy.svg)
