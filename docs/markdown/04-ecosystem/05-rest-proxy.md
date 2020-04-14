<!-- .slide: -->

# REST Proxy

* Composant développé par Confluent
* Propose de produire et consommer depuis Kafka via une API REST
* Permet l'intégration de systèmes ne pouvant communiquer que via HTTP
* Assure une compatibilité avec tous les langages de programmation

##==##
<!-- .slide: class="with-code" -->

# Producteur REST Proxy

```python
url = "http://localhost:8082/topics/customers"
headers = {
  "Content-Type": "application/vnd.kafka.json.v2+json"
}
payload = {
  "records":[{
    "key": "1",
    "value": { "id": 1, "email": "john.doe@gmail.com", "gender": "MALE" }
  }]
}
requests.post(url, headers=headers, data=json.dumps(payload))
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Producteur REST Proxy - Avro

```python
url = "http://localhost:8082/topics/customers"
headers = {
  "Content-Type": "application/vnd.kafka.avro.v2+json"
}
payload = {
  "value_schema_id": 1,
  "records": [{
    "key": "1",
    "value": { "id": 1, "email": "john.doe@gmail.com", "gender": "MALE" }
  }]
}
requests.post(url, headers=headers, data=json.dumps(payload))
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consommateur REST Proxy

1) Création de l'instance de consommation

```python
url = "http://localhost:8082/consumers/my_consumer_group"
headers = {
  "Content-Type": "application/vnd.kafka.v2+json"
}
payload = {
  "name": "my_consumer_instance",
  "format": "json",
  "auto.offset.reset": "earliest"
}
requests.post(url, headers=headers, data=json.dumps(payload))
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consommateur REST Proxy (Suite)

2) Création de la souscription

```python
url = "http://localhost:8082/consumers/my_consumer_group/instances/my_consumer_instance/subscription"
headers = {
  "Content-Type": "application/vnd.kafka.v2+json"
}
payload = {
  "topics": [
    "customers"
  ]
}
requests.post(url, headers=headers, data=json.dumps(payload))
```

<!-- .element class="big-code" -->

##==##
<!-- .slide: class="with-code" -->

# Consommateur REST Proxy (Suite)

3) Polling des messages

```python
url = "http://localhost:8082/consumers/my_consumer_group/instances/my_consumer_instance/records"
headers = {
  "Content-Type": "application/vnd.kafka.json.v2+json"
}
requests.get(url, headers=headers)
```

<!-- .element class="big-code" -->

```json
[{
  "key": "1",
  "value": { "id": 1, "email": "john.doe@gmail.com", "gender": "MALE" },
  ...
}]
```

<!-- .element class="big-code" -->