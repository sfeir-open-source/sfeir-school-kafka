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

kafka-topics \
--zookeeper zookeeper:2181 \
--create \
--topic suspicious_orders \
--partitions 3 \
--replication-factor 1 \
--if-not-exists
