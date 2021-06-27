<!-- .slide: -->

# Messages

**Message**

* Unité de base dans Kafka
* Couple clé / valeur (+ headers)
* Stocké au format binaire
* Egalement appelé `record` ou `log`

<br>

**Batch**

* Collection de messages à envoyer
* Réduit les aller-retours réseaux
* Permet une compression efficace (`gzip`, `snappy`, `lz4`, `zstd`)

##==##
<!-- .slide: class="with-code" -->

# Messages

```
// Record at offset 1 of topic customers
------- metadata -------
topic: customers
partition: 0
offset: 1
timestamp: 1585906100000
------- headers --------
X-Correlation-Id: 04734ee1-db49-4a67-bc1f-fb42c1e1c2fb
------- record ---------
key: 1
value: {"id": 1, "email": "john.doe@gmail.com", "gender": "MALE"}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Format

* Le format binaire est l'unique format autorisé par Kafka
* Le producteur doit sérialiser le message en binaire
* Le consommateur doit dé sérialiser le message vers le format désiré
* Convertisseurs natifs pour les types primitifs: (`Integer`, `String`, `Boolean`)

<br><br>

Comment gérer des types plus complexes ?

<!-- .element: class="center-big" -->

##==##
<!-- .slide: -->

# Format

* Possible de créer ses propres convertisseurs
* Permet de convertir un POJO en JSON avec Jackson par exemple
* Transparent pour le développeur

<br><br>

Comment valider le format de données ?

<!-- .element: class="center-big" -->
