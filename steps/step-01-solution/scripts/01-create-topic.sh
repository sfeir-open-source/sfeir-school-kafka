#!/usr/bin/env bash

kafka-topics \
--zookeeper zookeeper-1:2181 \
--create \
--topic customers \
--partitions 3 \
--replication-factor 2 \
--config cleanup.policy=delete,compact \
--config retention.ms=300000 \
--if-not-exist
