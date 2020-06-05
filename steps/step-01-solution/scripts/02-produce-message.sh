#!/usr/bin/env bash

cat ../data/customer.txt | \
kafka-console-producer \
--broker-list kafka-1:9092 \
--topic customers \
--property "parse.key=true" \
--property "key.separator=#"
