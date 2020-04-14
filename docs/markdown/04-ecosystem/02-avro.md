<!-- .slide: -->

# Avro

* Framework de sérialisation de données utilisé initialement par Hadoop
* Fonctionne grâce à un schéma Avro au format JSON avec l'extension `.avsc`
* Convertit des données binaires vers un POJO et vice-versa
* Validation de la rétro compatibilité d'un nouveau schéma

##==##
<!-- .slide: class="with-code" -->

# Schéma Avro

```json
{
  "type": "record",
  "namespace": "com.sfeir.kafka",
  "name": "Customer",
  "fields": [
    {
      "name": "id",
      "type": "integer"
    },
    {
      "name": "email",
      "type": "string"
    },
    {
      "name": "gender",
      "type": {
        "type": "enum",
        "name": "Gender",
        "symbols": ["MALE", "FEMALE"]
      }
    }
  ]
}
```

##==##
<!-- .slide -->

# Datum Readers

* **Generic**
  * le message Avro est récupéré sous forme de "map"
  * mapping manuel avec un objet Java

* **Specific**
  * le message est automatiquement converti en un objet Java
  * nécessite de générer les classes en amont à partir du schéma Avro

##==##
<!-- .slide: class="with-code" -->

# Avro avec Maven

Ajout du plugin Avro:

```xml
<plugin>
  <groupId>org.apache.avro</groupId>
  <artifactId>avro-maven-plugin</artifactId>
  <version>${avro.version}</version>
</plugin>
```

<!-- .element: class="big-code" -->

Génération des classes Avro à partir du schéma:

```bash
$ mvn clean install
```

<!-- .element: class="big-code" -->

##==##
<!-- .slide: -->

# Avantages d'Avro

* En envoyant le schéma, chaque message peut être dé serialisé par un consommateur
* L'object Avro peut être converti en objet Java pour faciliter la manipulation
* Le format Avro est un format binaire, ce qui réduit l'espace occupé sur disque et l'utilisation de la bande passante

##==##
<!-- .slide: -->

# Inconvénients d'Avro

* Chaque message doit embarquer son schéma Avro
* Chaque message doit être sérialisé en binaire
* Chaque message doit être dé-sérialisé en objet Java
* Le producteur doit s'assurer de la rétro compatibilité des données qu'il envoie
* Les consommateurs doivent pouvoir gérer des montées de version non cassantes du schéma
* Librairie Avro un peu complexe à utiliser

<p><br></p>

**BEAUCOUP** de code boilerplate à gérer par les clients!