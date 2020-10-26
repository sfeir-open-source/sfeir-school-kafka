#!/usr/bin/env bash

kafka-topics \
--zookeeper zookeeper:2181 \
--create \
--topic customers \
--partitions 3 \
--replication-factor 1 \
--config cleanup.policy=delete,compact \
--config retention.ms=300000 \
--if-not-exists
