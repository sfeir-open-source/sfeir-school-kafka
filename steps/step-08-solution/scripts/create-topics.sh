#!/usr/bin/env bash

kafka-topics \
--zookeeper zookeeper:2181 \
--create \
--topic customers \
--partitions 3 \
--replication-factor 1 \
--config "cleanup.policy=compact" \
--if-not-exists

kafka-topics \
--zookeeper zookeeper:2181 \
--create \
--topic orders \
--partitions 3 \
--replication-factor 1 \
--if-not-exists

kafka-topics \
--zookeeper zookeeper:2181 \
--create \
--topic card_payments \
--partitions 3 \
--replication-factor 1 \
--if-not-exists

< /scripts/data/customers.txt \
kafka-console-producer \
--broker-list kafka:9092 \
--topic customers \
--property "parse.key=true" \
--property "key.separator=#" \
--property "key.serializer=org.apache.kafka.common.serialization.IntegerSerializer"

< /scripts/data/orders.txt \
kafka-console-producer \
--broker-list kafka:9092 \
--topic orders \
--property "parse.key=true" \
--property "key.separator=#" \
--property "key.serializer=org.apache.kafka.common.serialization.StringSerializer"

< /scripts/data/card_payments.txt \
kafka-console-producer \
--broker-list kafka:9092 \
--topic card_payments \
--property "parse.key=true" \
--property "key.separator=#" \
--property "key.serializer=org.apache.kafka.common.serialization.StringSerializer"
