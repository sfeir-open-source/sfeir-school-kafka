#!/usr/bin/env bash

kafka-console-consumer \
--bootstrap-server kafka:9092 \
--topic customers \
--max-messages 1 \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property "print.key=true" \
--property "print.value=true"
