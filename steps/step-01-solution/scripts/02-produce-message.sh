#!/usr/bin/env bash

< /scripts/data/customers.txt \
kafka-console-producer \
--broker-list kafka:9092 \
--topic customers \
--property "parse.key=true" \
--property "key.separator=#"
