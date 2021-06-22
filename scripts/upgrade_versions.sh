#!/usr/bin/env bash

KAFKA_VERSION=2.8.0
CONFLUENT_VERSION=6.2.0

# Replace image versions in docker-compose files
for component in kafka zookeeper schema-registry kafka-connect; do
  sed -i '' "s/confluentinc\/cp-$component:.*/confluentinc\/cp-$component:$CONFLUENT_VERSION/g" steps/**/docker-compose.yml
done

# Replace version in properties of pom files
sed -i '' "s/<kafka\.version>.*<\/kafka\.version>/<kafka\.version>$KAFKA_VERSION<\/kafka\.version>/g" steps/**/pom.xml
sed -i '' "s/<confluent\.version>.*<\/confluent\.version>/<confluent\.version>$CONFLUENT_VERSION<\/confluent\.version>/g" steps/**/pom.xml

# Check if step solutions compiling
for solution in $(find steps/*-solution -type d -depth 0); do
  if [ -f "$solution/pom.xml" ]; then
    (cd "$solution" && mvn clean compile)
  fi
done
