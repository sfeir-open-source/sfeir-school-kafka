#!/usr/bin/env bash

confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.2.0
confluent-hub install --no-prompt confluentinc/kafka-connect-elasticsearch:5.4.0
