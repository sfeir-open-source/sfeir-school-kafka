<!-- .slide: -->

# Messages

**Message**

* Unité de base dans Kafka
* Consiste en un couple clé / valeur (+ headers)
* Stocké dans Kafka comme `byte[]`
* Egalement appelé `record` ou `log`

<br>

**Batch**

* Collection de messages à envoyer
* Réduit les aller-retours réseaux
* Permet une compression efficace (`gzip`, `snappy`, `lz4`)

##==##
<!-- .slide: class="with-code" -->

# Messages

```
// Record at offset 472 of topic customers
------- metadata -------
topic: customers
partition: 0
offset: 472
timestamp: 1585906100000
------- headers --------
X-Correlation-Id: dfnjg7oiNYk
------- record ---------
key: john doe
value: {"firstName":"john","lastName":"doe"}
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Format

* Tout type de format accepté à condition de sérialiser en binaire avant de produire dans Kafka
* Responsabilité des producteurs et consommateurs d'effectuer la conversion
* Kafka fournit des convertisseurs pour les types primitifs: (`Integer`, `String`, `Boolean`)

<br><br>

Comment gérer des types plus complexes ? 😱

<!-- .element: class="center-big" -->

##==##
<!-- .slide: -->

# Format

* Possibilité de créer ses propres convertisseurs
* Permet de convertir un POJO en JSON avec Jackson par exemple
* Transparent pour le développeur

<br><br>

Mais comment valider le format de données ? 🤔

<!-- .element: class="center-big" -->
